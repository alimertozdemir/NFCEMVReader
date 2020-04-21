package com.gt.alimert.emvcardreader.lib;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.IsoDep;
import android.util.Log;
import android.widget.Toast;

import com.gt.alimert.emvcardreader.lib.enums.CardType;
import com.gt.alimert.emvcardreader.lib.exception.CommandException;
import com.gt.alimert.emvcardreader.lib.model.AflObject;
import com.gt.alimert.emvcardreader.lib.model.ApduResponse;
import com.gt.alimert.emvcardreader.lib.model.Card;
import com.gt.alimert.emvcardreader.lib.model.LogMessage;
import com.gt.alimert.emvcardreader.lib.util.AflUtil;
import com.gt.alimert.emvcardreader.lib.util.AidUtil;
import com.gt.alimert.emvcardreader.lib.util.ApduUtil;
import com.gt.alimert.emvcardreader.lib.util.CardUtil;
import com.gt.alimert.emvcardreader.lib.util.GpoUtil;
import com.gt.alimert.emvcardreader.lib.util.HexUtil;
import com.gt.alimert.emvcardreader.lib.util.TlvTagConstant;
import com.gt.alimert.emvcardreader.lib.util.TlvUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author AliMertOzdemir
 * @class CtlessCardService
 * @created 20.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public class CtlessCardService implements NfcAdapter.ReaderCallback {

    private static String TAG = CtlessCardService.class.getName();

    private Activity mContext;
    private NfcAdapter mNfcAdapter;
    private IsoDep mIsoDep;

    private ResultListener mResultListener;

    //  reader mode flags: listen for type A (not B), skipping ndef check
    private static final int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A |
                    NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK |
                    NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS;

    private static final int SW_NO_ERROR = 0x9000;
    private int READ_TIMEOUT = 30000;
    private List<LogMessage> mLogMessages;
    private Card mCard;

    private CtlessCardService() {}

    public CtlessCardService(Activity context, ResultListener resultListener) {
        this.mContext = context;
        this.mResultListener = resultListener;
    }

    public void start() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(mContext);
        // Check if the device has NFC
        if (mNfcAdapter == null) {
            Toast.makeText(mContext, "NFC not supported", Toast.LENGTH_LONG).show();
        }
        // Check if NFC is enabled on device
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(mContext, "Enable NFC before using the app",
                    Toast.LENGTH_LONG).show();
        } else {
            mNfcAdapter.enableReaderMode(mContext, this, READER_FLAGS, null);
        }
    }

    public void setTimeout(int timeoutMillis) {
        READ_TIMEOUT = timeoutMillis;
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        mLogMessages = new ArrayList<>();
        mCard = new Card();

        String[] tagList = tag.getTechList();
        for (String tagName : tagList) {
            Log.d(TAG, "TAG NAME : " + tagName);
        }

        mIsoDep = IsoDep.get(tag);

        if(mIsoDep != null && mIsoDep.getTag() != null) {
            Log.d(TAG, "ISO-DEP - Compatible NFC tag discovered: " + mIsoDep.getTag());

            mResultListener.onCardDetect();

            try {
                mIsoDep.connect();
                mIsoDep.setTimeout(READ_TIMEOUT);
                byte[] command = ApduUtil.selectPpse();
                byte[] result = mIsoDep.transceive(command);
                byte[] responseData = evaluateResult( "SELECT PPSE", command, result);

                byte[] aid = TlvUtil.getTlvByTag(responseData, TlvTagConstant.AID_TLV_TAG);
                if(!AidUtil.isApprovedAID(aid)) {
                    throw new CommandException("AID NOT SUPPORTED -> " + HexUtil.bytesToHexadecimal(aid));
                }
                command = ApduUtil.selectApplication(aid);
                result = mIsoDep.transceive(command);
                responseData = evaluateResult("SELECT APPLICATION", command, result);

                byte[] pdol = TlvUtil.getTlvByTag(responseData, TlvTagConstant.PDOL_TLV_TAG);
                pdol = GpoUtil.generatePdol(pdol);
                command = ApduUtil.getProcessingOption(pdol);
                result = mIsoDep.transceive(command);
                responseData = evaluateResult("GET PROCESSING OPTION", command, result);

                byte[] tag80 = TlvUtil.getTlvByTag(responseData, TlvTagConstant.GPO_RMT1_TLV_TAG);
                byte[] tag77 = TlvUtil.getTlvByTag(responseData, TlvTagConstant.GPO_RMT2_TLV_TAG);

                byte[] aflData = null;
                if(tag80 != null) {
                    aflData = tag80;
                    aflData = Arrays.copyOfRange(aflData, 2, aflData.length);
                } else if(tag77 != null) {
                    extractTrack2Data(tag77);
                    aflData = TlvUtil.getTlvByTag(responseData, TlvTagConstant.AFL_TLV_TAG);
                }

                if(aflData != null) {
                    Log.d(TAG, "AFL HEX DATA -> " + HexUtil.bytesToHexadecimal(aflData));
                    List<AflObject> aflDatas = AflUtil.getAflDataRecords(aflData);
                    if(aflDatas != null && !aflDatas.isEmpty()) {
                        for (AflObject aflObject : aflDatas) {
                            command = aflObject.getReadCommand();
                            result = mIsoDep.transceive(command);
                            responseData = evaluateResult("READ RECORD (sfi: " + aflObject.getSfi() + " record: " + aflObject.getRecordNumber() + ")", command, result);
                            extractTrack2Data(responseData);
                        }
                    }
                } else {
                    Log.d(TAG, "AFL HEX DATA -> NULL");
                }

                if(mCard.getTrack2() != null) {
                    CardType cardType = AidUtil.getCardBrandByAID(aid);
                    mCard.setCardBrand(cardType.getCardBrand());
                }

                command = ApduUtil.getReadTlvData(TlvTagConstant.PIN_TRY_COUNTER_TLV_TAG);
                result = mIsoDep.transceive(command);
                evaluateResult("PIN TRY COUNT", command, result);

                command = ApduUtil.getReadTlvData(TlvTagConstant.ATC_TLV_TAG);
                result = mIsoDep.transceive(command);
                evaluateResult("APPLICATION TRANSACTION COUNTER", command, result, true);

            } catch (CommandException e) {
                Log.d(TAG, "COMMAND EXCEPTION -> " + e.getLocalizedMessage());
                mResultListener.onCardReadFail("COMMAND EXCEPTION -> " + e.getLocalizedMessage());
            } catch (TagLostException e) {
                Log.d(TAG, "ISO DEP TAG LOST ERROR -> " + e.getLocalizedMessage());
                mResultListener.onCardMovedSoFast();
            } catch (IOException e) {
                Log.d(TAG, "ISO DEP CONNECT ERROR -> " + e.getLocalizedMessage());
                mResultListener.onCardReadFail("ISO DEP CONNECT ERROR -> " + e.getLocalizedMessage());
            }

        } else {
            Log.d(TAG, "ISO DEP is null");
            mResultListener.onCardReadFail("ISO DEP is null");
        }
    }

    private byte[] evaluateResult(String commandName, byte[] command, byte[] result) throws IOException {
        return evaluateResult(commandName, command, result, false);
    }

    private byte[] evaluateResult(String commandName, byte[] command, byte[] result, boolean isLastCommand) throws IOException {
        ApduResponse apduResponse = new ApduResponse(result);
        returnMessage(commandName, HexUtil.bytesToHexadecimal(command), HexUtil.bytesToHexadecimal(result), isLastCommand);
        Log.d(TAG, commandName + " REQUEST : " + HexUtil.bytesToHexadecimal(command));
        if(apduResponse.isStatus(SW_NO_ERROR)) {
            Log.d(TAG, commandName + " RESULT : " + HexUtil.bytesToHexadecimal(result));
            return apduResponse.getData();
        } else {
            String error = commandName + " ERROR : " + HexUtil.bytesToHexadecimal(result);
            Log.d(TAG, "COMMAND EXCEPTION -> " + error);
            return apduResponse.getData();
        }
    }

    private void extractTrack2Data(byte[] responseData) {
        byte[] track2 = TlvUtil.getTlvByTag(responseData, TlvTagConstant.TRACK2_TLV_TAG);
        byte[] pan = TlvUtil.getTlvByTag(responseData, TlvTagConstant.APPLICATION_PAN_TLV_TAG);

        if(track2 != null && mCard.getTrack2() == null) {
            mCard = CardUtil.getCardInfoFromTrack2(track2);
        }

        if(pan != null && mCard.getPan() == null) {
            mCard.setPan(HexUtil.bytesToHexadecimal(pan));
        }
    }

    private void readExtraRecord() throws IOException {

        byte[] command = ApduUtil.getReadTlvData(TlvTagConstant.AMOUNT_AUTHORISED_TLV_TAG);
        byte[] result = mIsoDep.transceive(command);
        evaluateResult("AMOUNT AUTHORIZED", command, result);

        command = ApduUtil.getReadTlvData(TlvTagConstant.AMOUNT_OTHER_TLV_TAG);
        result = mIsoDep.transceive(command);
        evaluateResult("AMOUNT OTHER", command, result);

        command = ApduUtil.getReadTlvData(TlvTagConstant.PAYPASS_LOG_FORMAT_TLV_TAG);
        result = mIsoDep.transceive(command);
        evaluateResult("PAYPASS LOG FORMAT", command, result);

        command = ApduUtil.getReadTlvData(TlvTagConstant.PAYWAVE_LOG_FORMAT_TLV_TAG);
        result = mIsoDep.transceive(command);
        evaluateResult("PAYWAVE LOG FORMAT", command, result);

        command = ApduUtil.verifyPin(null);
        result = mIsoDep.transceive(command);
        evaluateResult("VERIFY PIN", command, result);

        /*byte[] pdol = HexUtil.hexadecimalToBytes("9F66049F02069F03069F1A0295055F2A029A039C019F3704");
        byte[] extraPdol = GpoUtil.generatePdol(pdol);
        command = ApduUtil.getProcessingOption(pdol);
        result = mIsoDep.transceive(command);
        evaluateResult("GET EXTRA PDOL", command, result);*/
    }

    private void returnMessage(String commandName, String request, String response, boolean isLastCommand) throws IOException {
        String reqMessage = request.replaceAll("..", "$0 ");
        String respMessage = response.replaceAll("..", "$0 ");

        LogMessage logMessage = new LogMessage(commandName, reqMessage, respMessage);
        mLogMessages.add(logMessage);

        if(isLastCommand) {
            mCard.setLogMessages(mLogMessages);
            mResultListener.onCardReadSuccess(mCard);
            mIsoDep.close();
            mNfcAdapter.disableReaderMode(mContext);
        }

    }

    public interface ResultListener {
        void onCardDetect();
        void onCardReadSuccess(Card card);
        void onCardReadFail(String error);
        void onCardMovedSoFast();
    }

}
