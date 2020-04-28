package com.gt.alimert.emvnfclib;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.IsoDep;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.gt.alimert.emvnfclib.data.model.TransactionRequest;
import com.gt.alimert.emvnfclib.data.remote.ApiService;
import com.gt.alimert.emvnfclib.data.remote.NetworkManager;
import com.gt.alimert.emvnfclib.enums.CardType;
import com.gt.alimert.emvnfclib.enums.TransactionType;
import com.gt.alimert.emvnfclib.exception.CommandException;
import com.gt.alimert.emvnfclib.model.AflObject;
import com.gt.alimert.emvnfclib.model.AipObject;
import com.gt.alimert.emvnfclib.model.ApduResponse;
import com.gt.alimert.emvnfclib.model.Application;
import com.gt.alimert.emvnfclib.model.CVMList;
import com.gt.alimert.emvnfclib.model.CVRule;
import com.gt.alimert.emvnfclib.model.Card;
import com.gt.alimert.emvnfclib.model.EmvParam;
import com.gt.alimert.emvnfclib.model.LogMessage;
import com.gt.alimert.emvnfclib.model.TvrObject;
import com.gt.alimert.emvnfclib.util.AflUtil;
import com.gt.alimert.emvnfclib.util.AidUtil;
import com.gt.alimert.emvnfclib.util.ApduUtil;
import com.gt.alimert.emvnfclib.util.CardUtil;
import com.gt.alimert.emvnfclib.util.DeviceUtil;
import com.gt.alimert.emvnfclib.util.DolUtil;
import com.gt.alimert.emvnfclib.util.EmvUtil;
import com.gt.alimert.emvnfclib.util.HexUtil;
import com.gt.alimert.emvnfclib.util.SharedPrefUtil;
import com.gt.alimert.emvnfclib.util.TlvTagConstant;
import com.gt.alimert.emvnfclib.util.TlvUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    private int READ_TIMEOUT = 3000;
    private int CONNECT_TIMEOUT = 30000;
    private String mAmount = "10000";
    private TransactionType mTransactionType;
    private CountDownTimer mTimer;
    private List<LogMessage> mLogMessages;
    private int mUserAppIndex = -1;
    private Card mCard;
    private EmvParam mEmvParam;
    private String mEndpoint = "https://gbmposapp-d.garanti.com.tr/api/";
    private ApiService mApiService;

    private CtlessCardService() {}

    public CtlessCardService(Activity context, ResultListener resultListener) {
        this.mContext = context;
        this.mResultListener = resultListener;
    }

    public void startTransaction(TransactionType transactionType, String amount) {
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

        if(mApiService == null) {
            NetworkManager networkManager = new NetworkManager(mEndpoint, CONNECT_TIMEOUT);
            mApiService = networkManager.init();
        }

        this.mAmount = amount;
        this.mTransactionType = transactionType;

    }

    public void setEndpoint(String endpoint) {
        this.mEndpoint = endpoint;
    }

    public void setTimeout(int timeoutMillis) {
        CONNECT_TIMEOUT = timeoutMillis;
    }

    public void setSelectedApplication(int index) {
        this.mUserAppIndex = index;
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        mLogMessages = new ArrayList<>();
        mCard = new Card();
        mEmvParam = new EmvParam();

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

                byte[] amount = HexUtil.addLeftSpaceZeros(mAmount, 12);
                byte[] tranType = HexUtil.hexadecimalToBytes(mTransactionType.getCode());
                mAmount = "0";

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

                byte[] dfName = TlvUtil.getTlvByTag(responseData, TlvTagConstant.DEDICATED_FILE_NAME_TLV_TAG);
                byte[] pdol = TlvUtil.getTlvByTag(responseData, TlvTagConstant.PDOL_TLV_TAG);
                pdol = DolUtil.generateDol(pdol, amount);
                command = ApduUtil.getProcessingOption(pdol);
                result = mIsoDep.transceive(command);
                responseData = evaluateResult("GET PROCESSING OPTION", command, result);

                byte[] tag80 = TlvUtil.getTlvByTag(responseData, TlvTagConstant.GPO_RMT1_TLV_TAG);
                byte[] tag77 = TlvUtil.getTlvByTag(responseData, TlvTagConstant.GPO_RMT2_TLV_TAG);

                byte[] aflData = null, aipData = null;
                if(tag77 != null) {
                    extractTrack2Data(tag77);
                    aipData = TlvUtil.getTlvByTag(responseData, TlvTagConstant.AIP_TLV_TAG);
                    aflData = TlvUtil.getTlvByTag(responseData, TlvTagConstant.AFL_TLV_TAG);
                    extractEmvData(responseData);
                } else if(tag80 != null) {
                    aflData = tag80;
                    aipData = Arrays.copyOfRange(aflData, 0, 2);
                    aflData = Arrays.copyOfRange(aflData, 2, aflData.length);
                }

                byte[] cvmList = null, cdol1 = null, cdol2 = null;
                if(aflData != null) {
                    Log.d(TAG, "AFL HEX DATA -> " + HexUtil.bytesToHexadecimal(aflData));
                    List<AflObject> aflDatas = AflUtil.getAflDataRecords(aflData);
                    if(aflDatas != null && !aflDatas.isEmpty() && aflDatas.size() < 10) {
                        Log.d(TAG, "AFL DATA SIZE -> " + aflDatas.size());
                        for (AflObject aflObject : aflDatas) {
                            command = aflObject.getReadCommand();
                            result = mIsoDep.transceive(command);
                            responseData = evaluateResult("READ RECORD (sfi: " + aflObject.getSfi() + " record: " + aflObject.getRecordNumber() + ")", command, result);
                            extractTrack2Data(responseData);
                            extractEmvData(responseData);
                            if(cvmList == null)
                                cvmList = TlvUtil.getTlvByTag(responseData, TlvTagConstant.CVM_LIST_TLV_TAG);
                            if(cdol1 == null)
                                cdol1 = TlvUtil.getTlvByTag(responseData, TlvTagConstant.CDOL_1_TLV_TAG);
                            if(cdol2 == null)
                                cdol2 = TlvUtil.getTlvByTag(responseData, TlvTagConstant.CDOL_2_TLV_TAG);
                        }

                        if (cdol1 != null) {
                            Log.d(TAG, "CDOL 1 DATA -> " + HexUtil.bytesToHexadecimal(cdol1));
                            cdol1 = DolUtil.generateDol(cdol1, amount);
                            command = ApduUtil.generateAC(cdol1);
                            result = mIsoDep.transceive(command);
                            responseData = evaluateResult("FIRST GENERATE AC", command, result);
                            extractEmvData(responseData);
                        }
                    }
                } else {
                    Log.d(TAG, "AFL HEX DATA -> NULL");
                }

                AipObject aipObject = new AipObject(aipData[0], aipData[1]);
                Log.d(TAG, "Application Interchange Profile (AIP) --> \n" +
                        aipObject.getSDASupportedString() + "\n" +
                        aipObject.getDDASupportedString() + "\n" +
                        aipObject.getCDASupportedString() + "\n" +
                        aipObject.getCardholderVerificationSupportedString() + "\n" +
                        aipObject.getIssuerAuthenticationIsSupportedString() + "\n"+
                        aipObject.getTerminalRiskManagementToBePerformedString() + "\n"
                );

                TvrObject tvrObject = new TvrObject();
                tvrObject.setOfflineDataAuthenticationWasNotPerformed(true);
                tvrObject.setPinEntryRequiredAndPINPadNotPresentOrNotWorking(true);
                if(aipObject.isCardholderVerificationSupported()) {
                    if(cvmList != null) {
                        CVMList cvmListObject = new CVMList(cvmList);
                        Log.d(TAG, "CVMList --> ");
                        for (CVRule rule: cvmListObject.getRules()) {
                            Log.d(TAG, rule.getRuleString());
                        }
                    } else {
                        tvrObject.setICCDataMissing(true);
                    }
                }

                byte[] stan = HexUtil.addLeftSpaceZeros(String.valueOf(SharedPrefUtil.getStan(mContext)), 8);
                mEmvParam.setTransactionSequenceCounter(stan);
                mEmvParam.setDedicatedFileName(dfName);
                mEmvParam.setTerminalVerificationResults(tvrObject.getBytes());
                mEmvParam.setAmountAuthorized(amount);
                mEmvParam.setTransactionType(tranType);
                mEmvParam.setApplicationInterchangeProfile(aipObject.getBytes());
                mEmvParam.setInterfaceDeviceSerialNumber(DeviceUtil.getAndroidId(mContext).getBytes());

                if(mCard.getTrack2() != null) {
                    CardType cardType = AidUtil.getCardBrandByAID(aid);
                    mCard.setCardType(cardType);
                } else {
                    throw new Exception("CALL YOUR BANK");
                }

                command = ApduUtil.getReadTlvData(TlvTagConstant.ISSUER_APPLICATION_DATA_TLV_TAG);
                result = mIsoDep.transceive(command);
                evaluateResult("ISSUER APPLICATION DATA", command, result);

                command = ApduUtil.getReadTlvData(TlvTagConstant.PIN_TRY_COUNTER_TLV_TAG);
                result = mIsoDep.transceive(command);
                evaluateResult("PIN TRY COUNT", command, result);

                command = ApduUtil.getReadTlvData(TlvTagConstant.ATC_TLV_TAG);
                result = mIsoDep.transceive(command);
                evaluateResult("APPLICATION TRANSACTION COUNTER", command, result, true);

                byte[] emvData = EmvUtil.generateEmvData(mEmvParam);
                mCard.setEmvData(HexUtil.bytesToHexadecimal(emvData));
                Log.d(TAG, "EMVDATA RESULT HEX --> " + HexUtil.bytesToHexadecimal(emvData));

                Disposable disposable = mApiService.startTransaction(getTransactionRequest())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {
                            if("00".equals(response.getResponseCode()))
                                mResultListener.onCardReadSuccess(mCard);
                            else
                                mResultListener.onCardReadFail(response.getGenericField().get("serverResponse"));
                            mIsoDep.close();
                            mNfcAdapter.disableReaderMode(mContext);
                        }, throwable -> {
                            mResultListener.onCardReadFail(throwable.getLocalizedMessage());
                            mIsoDep.close();
                            mNfcAdapter.disableReaderMode(mContext);
                        });

            } catch (CommandException e) {
                Log.d(TAG, "COMMAND EXCEPTION -> " + e.getLocalizedMessage());
                mResultListener.onCardReadFail("COMMAND EXCEPTION -> " + e.getLocalizedMessage());
            } catch (TagLostException e) {
                Log.d(TAG, "ISO DEP TAG LOST ERROR -> " + e.getLocalizedMessage());
                mResultListener.onCardMovedSoFast();
            } catch (IOException e) {
                Log.d(TAG, "ISO DEP CONNECT ERROR -> " + e.getLocalizedMessage());
                mResultListener.onCardReadFail("ISO DEP CONNECT ERROR -> " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.d(TAG, "CARD ERROR -> " + e.getLocalizedMessage());
                mResultListener.onCardReadFail("CARD ERROR -> " + e.getLocalizedMessage());
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

    private void extractEmvData(byte[] responseData) {
        byte[] issuerAppData = TlvUtil.getTlvByTag(responseData, TlvTagConstant.ISSUER_APPLICATION_DATA_TLV_TAG);
        if(issuerAppData != null)
            mEmvParam.setIssuerApplicationData(issuerAppData);
        byte[] appCryptogram = TlvUtil.getTlvByTag(responseData, TlvTagConstant.APPLICATION_CRYPTOGRAM_TLV_TAG);
        if(appCryptogram != null)
            mEmvParam.setApplicationCryptogram(appCryptogram);
        byte[] panSeqNumber = TlvUtil.getTlvByTag(responseData, TlvTagConstant.PAN_SEQUENCE_NUMBER_TLV_TAG);
        if(panSeqNumber != null)
            mEmvParam.setPanSequenceNumber(panSeqNumber);
        byte[] atcData = TlvUtil.getTlvByTag(responseData, TlvTagConstant.ATC_TLV_TAG);
        if(atcData != null)
            mEmvParam.setApplicationTransactionCounter(atcData);
        byte[] cryptogramInfo = TlvUtil.getTlvByTag(responseData, TlvTagConstant.CRYPTOGRAM_INFORMATION_DATA_TLV_TAG);
        if(cryptogramInfo != null)
            mEmvParam.setCryptogramInformationData(cryptogramInfo);
    }

    private void readExtraRecord() throws IOException {

        byte[] command = ApduUtil.getReadTlvData(TlvTagConstant.AMOUNT_AUTHORISED_TLV_TAG);
        byte[] result = mIsoDep.transceive(command);
        evaluateResult("amount AUTHORIZED", command, result);

        command = ApduUtil.getReadTlvData(TlvTagConstant.AMOUNT_OTHER_TLV_TAG);
        result = mIsoDep.transceive(command);
        evaluateResult("amount OTHER", command, result);

        command = ApduUtil.getReadTlvData(TlvTagConstant.PAYPASS_LOG_FORMAT_TLV_TAG);
        result = mIsoDep.transceive(command);
        evaluateResult("PAYPASS LOG FORMAT", command, result);

        command = ApduUtil.getReadTlvData(TlvTagConstant.PAYWAVE_LOG_FORMAT_TLV_TAG);
        result = mIsoDep.transceive(command);
        evaluateResult("PAYWAVE LOG FORMAT", command, result);

        command = ApduUtil.verifyPin(null);
        result = mIsoDep.transceive(command);
        evaluateResult("VERIFY PIN", command, result);

    }

    private void returnMessage(String commandName, String request, String response, boolean isLastCommand) throws IOException {
        String reqMessage = request.replaceAll("..", "$0 ");
        String respMessage = response.replaceAll("..", "$0 ");

        LogMessage logMessage = new LogMessage(commandName, reqMessage, respMessage);
        mLogMessages.add(logMessage);

        if(isLastCommand)
            mCard.setLogMessages(mLogMessages);


    }

    private byte[] getAidFromMultiApplicationCard(byte[] responseData) {

        byte[] aid = null;
        // *** FIND MULTIPLE APPLICATIONS ***//
        List<Application> appList = TlvUtil.getApplicationList(responseData);

        if(appList.size() > 1) {

            if(mUserAppIndex != -1 && appList.size() > mUserAppIndex) {
                aid = appList.get(mUserAppIndex).getAid();
                mUserAppIndex = -1;
            } else {
                mResultListener.onCardSelectApplication(appList);
            }

        } else {
            aid = appList.get(0).getAid();
        }

        return aid;
    }

    private TransactionRequest getTransactionRequest() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCardBrand(mCard.getCardType().getCardBrand());
        transactionRequest.setCardType("F");
        transactionRequest.setEmvData(mCard.getEmvData());
        transactionRequest.setPan(mCard.getPan());
        transactionRequest.setExpiryDate(mCard.getExpireDate());
        transactionRequest.setTrack2Data(mCard.getTrack2());
        transactionRequest.setTransactionType(TransactionType.SALES.name());
        transactionRequest.setAmount(HexUtil.bytesToHexadecimal(mEmvParam.getAmountAuthorized()));
        transactionRequest.setCurrencyCode("949");
        transactionRequest.setTerminalId("30691802");
        transactionRequest.setMerchantId("000000000982066");
        Map<String, String> genericField = new HashMap<>();
        genericField.put("orderId", "");
        genericField.put("ipAddress", "192.168.1.101");
        genericField.put("terminalId", "30691802");
        genericField.put("X-MPOS-Request-Header", "{\"appVersion\":1011,\"deviceId\":\"498EA0D040769BB4FB9BE73CE9E32A4C654C82C17515D5DF91EF5240BF51160F\",\"requestToken\":0,\"sessionId\":\"f4e4a57c-f876-4367-af15-f13de32120c5\",\"terminalNum\":30691802,\"timestamp\":\"1587810936\",\"userOpaqueId\":\"R2RyR043bXN2cWdnYjRORnN3d3lWdz09\"}");
        transactionRequest.setGenericField(genericField);
        return transactionRequest;
    }

    public interface ResultListener {
        void onCardDetect();
        void onCardReadSuccess(Card card);
        void onCardReadFail(String error);
        void onCardReadTimeout();
        void onCardMovedSoFast();
        void onCardSelectApplication(List<Application> applications);
    }

}
