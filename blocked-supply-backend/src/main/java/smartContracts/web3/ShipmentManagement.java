package smartContracts.web3;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/hyperledger/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.12.1.
 */
@SuppressWarnings("rawtypes")
public class ShipmentManagement extends Contract {
    public static final String BINARY = "608060405260015f55600180553480156016575f5ffd5b50610df9806100245f395ff3fe608060405234801561000f575f5ffd5b5060043610610079575f3560e01c806398facc3b1161005857806398facc3b146100bf578063a896fe45146100d2578063b465a0f2146100e5578063fad8919f146100ec575f5ffd5b80623a4a0f1461007d57806304758111146100a2578063092b3740146100aa575b5f5ffd5b61009061008b3660046107e2565b6100ff565b60405190815260200160405180910390f35b600154610090565b6100bd6100b83660046107e2565b61018b565b005b6100bd6100cd3660046107f9565b6102a4565b6100bd6100e03660046108b8565b61034c565b5f54610090565b6100bd6100fa366004610958565b6105af565b5f815f81116101295760405162461bcd60e51b815260040161012090610a4e565b60405180910390fd5b5f5481106101745760405162461bcd60e51b815260206004820152601860248201527729b434b836b2b73a103237b2b9903737ba1032bc34b9ba1760411b6044820152606401610120565b5f8381526003602052604090205491505b50919050565b805f81116101ab5760405162461bcd60e51b815260040161012090610a4e565b5f5481106101f65760405162461bcd60e51b815260206004820152601860248201527729b434b836b2b73a103237b2b9903737ba1032bc34b9ba1760411b6044820152606401610120565b5f60025f8481526020019081526020015f209050805f01547fd971ffcf5cbed286d16eb9cb90509d330418947e2cc832c8bda5dd2b4fbf8c7a826001018360020184600301856004018660050187600601548860070154896008015f9054906101000a900460ff16600381111561026f5761026f610a91565b60088b015460405161029799989796959493929161010090046001600160a01b031690610b56565b60405180910390a2505050565b5f8281526003602052604081208054839081106102c3576102c3610beb565b905f5260205f2090600702019050805f01547fcd3a3f2736fc1d326c90624a647d8290aaa56c1bc81375e79ca971e40da12d6582600101548360020154846003015f9054906101000a900460ff16600381111561032257610322610a91565b60058601546040516102979493929160048901916001600160a01b039091169060068a0190610bff565b5f85815260026020526040902060080154859061010090046001600160a01b031633146103d35760405162461bcd60e51b815260206004820152602f60248201527f4f6e6c79207468652063757272656e74206f776e65722063616e20706572666f60448201526e3936903a3434b99030b1ba34b7b71760891b6064820152608401610120565b5f868152600260205260409020600881018054610100600160a81b031981166101006001600160a01b038a160290811783558792916001600160a81b03191660ff1990911617600183600381111561042d5761042d610a91565b0217905550600180545f918261044283610c51565b91905055905060035f8981526020019081526020015f206040518060e001604052808381526020018a815260200142815260200188600381111561048857610488610a91565b815260208082018990526001600160a01b038b1660408084019190915260609283018990528454600181810187555f968752958390208551600790920201908155918401518286015583015160028201559082015160038083018054949593949293909260ff191691849081111561050257610502610a91565b02179055506080820151600482019061051b9082610cc1565b5060a08201516005820180546001600160a01b0319166001600160a01b0390921691909117905560c082015160068201906105569082610cc1565b5050507f8b787dd444d18433253cf8599645cbb4fe62e7cf6fa82ce67ab60f56ac3957708887600381111561058d5761058d610a91565b6040805192835260208301919091520160405180910390a15050505050505050565b5f82116105fe5760405162461bcd60e51b815260206004820152601d60248201527f556e697473206d7573742062652067726561746572207468616e20302e0000006044820152606401610120565b5f811161064d5760405162461bcd60e51b815260206004820152601e60248201527f576569676874206d7573742062652067726561746572207468616e20302e00006044820152606401610120565b5f8054818061065b83610c51565b9190505590506040518061014001604052808281526020018981526020018881526020018781526020018681526020018581526020018481526020018381526020015f60038111156106af576106af610a91565b8152336020918201525f8381526002825260409020825181559082015160018201906106db9082610cc1565b50604082015160028201906106f09082610cc1565b50606082015160038201906107059082610cc1565b506080820151600482019061071a9082610cc1565b5060a0820151600582019061072f9082610cc1565b5060c0820151600682015560e0820151600782015561010082015160088201805460ff1916600183600381111561076857610768610a91565b02179055506101208201518160080160016101000a8154816001600160a01b0302191690836001600160a01b03160217905550905050807f8e4c701ce5fb405651e38e1b8e96ea0a29ddec2fc923811b52b9c87f1da33ef485336040516107d0929190610d7c565b60405180910390a25050505050505050565b5f602082840312156107f2575f5ffd5b5035919050565b5f5f6040838503121561080a575f5ffd5b50508035926020909101359150565b634e487b7160e01b5f52604160045260245ffd5b5f82601f83011261083c575f5ffd5b813567ffffffffffffffff81111561085657610856610819565b604051601f8201601f19908116603f0116810167ffffffffffffffff8111828210171561088557610885610819565b60405281815283820160200185101561089c575f5ffd5b816020850160208301375f918101602001919091529392505050565b5f5f5f5f5f60a086880312156108cc575f5ffd5b8535945060208601356001600160a01b03811681146108e9575f5ffd5b93506040860135600481106108fc575f5ffd5b9250606086013567ffffffffffffffff811115610917575f5ffd5b6109238882890161082d565b925050608086013567ffffffffffffffff81111561093f575f5ffd5b61094b8882890161082d565b9150509295509295909350565b5f5f5f5f5f5f5f60e0888a03121561096e575f5ffd5b873567ffffffffffffffff811115610984575f5ffd5b6109908a828b0161082d565b975050602088013567ffffffffffffffff8111156109ac575f5ffd5b6109b88a828b0161082d565b965050604088013567ffffffffffffffff8111156109d4575f5ffd5b6109e08a828b0161082d565b955050606088013567ffffffffffffffff8111156109fc575f5ffd5b610a088a828b0161082d565b945050608088013567ffffffffffffffff811115610a24575f5ffd5b610a308a828b0161082d565b979a969950949793969560a0850135955060c0909401359392505050565b60208082526023908201527f536869706d656e74204944206d7573742062652067726561746572207468616e60408201526210181760e91b606082015260800190565b634e487b7160e01b5f52602160045260245ffd5b600181811c90821680610ab957607f821691505b60208210810361018557634e487b7160e01b5f52602260045260245ffd5b5f8154610ae381610aa5565b808552600182168015610afd5760018114610b1957610b4d565b60ff1983166020870152602082151560051b8701019350610b4d565b845f5260205f205f5b83811015610b445781546020828a010152600182019150602081019050610b22565b87016020019450505b50505092915050565b61012081525f610b6a61012083018c610ad7565b8281036020840152610b7c818c610ad7565b90508281036040840152610b90818b610ad7565b90508281036060840152610ba4818a610ad7565b90508281036080840152610bb88189610ad7565b60a0840197909752505060c081019390935260e08301919091526001600160a01b03166101009091015295945050505050565b634e487b7160e01b5f52603260045260245ffd5b86815285602082015284604082015260c060608201525f610c2360c0830186610ad7565b6001600160a01b038516608084015282810360a0840152610c448185610ad7565b9998505050505050505050565b5f60018201610c6e57634e487b7160e01b5f52601160045260245ffd5b5060010190565b601f821115610cbc57805f5260205f20601f840160051c81016020851015610c9a5750805b601f840160051c820191505b81811015610cb9575f8155600101610ca6565b50505b505050565b815167ffffffffffffffff811115610cdb57610cdb610819565b610cef81610ce98454610aa5565b84610c75565b6020601f821160018114610d21575f8315610d0a5750848201515b5f19600385901b1c1916600184901b178455610cb9565b5f84815260208120601f198516915b82811015610d505787850151825560209485019460019092019101610d30565b5084821015610d6d57868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b604081525f83518060408401528060208601606085015e5f60608285018101919091526001600160a01b03949094166020840152601f01601f19169091019091019291505056fea2646970667358221220c48123df30514ccc87eff2eec5cbc81a50408038c620e6dddb7a8975fa1984e964736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATESHIPMENT = "createShipment";

    public static final String FUNC_GETNEXTSHIPMENTID = "getNextShipmentId";

    public static final String FUNC_GETNEXTTRANSFERID = "getNextTransferId";

    public static final String FUNC_GETSHIPMENT = "getShipment";

    public static final String FUNC_GETTRANSFERBYINDEX = "getTransferByIndex";

    public static final String FUNC_GETTRANSFERCOUNT = "getTransferCount";

    public static final String FUNC_SHIPMENTTRANSFER = "shipmentTransfer";

    public static final Event SHIPMENTCREATED_EVENT = new Event("ShipmentCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event SHIPMENTRETRIEVED_EVENT = new Event("ShipmentRetrieved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event TRANSFERCREATED_EVENT = new Event("TransferCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFERRETRIEVED_EVENT = new Event("TransferRetrieved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected ShipmentManagement(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ShipmentManagement(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ShipmentManagement(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ShipmentManagement(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<ShipmentCreatedEventResponse> getShipmentCreatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SHIPMENTCREATED_EVENT, transactionReceipt);
        ArrayList<ShipmentCreatedEventResponse> responses = new ArrayList<ShipmentCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ShipmentCreatedEventResponse typedResponse = new ShipmentCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.deliveryDate = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.currentOwner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ShipmentCreatedEventResponse getShipmentCreatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SHIPMENTCREATED_EVENT, log);
        ShipmentCreatedEventResponse typedResponse = new ShipmentCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.deliveryDate = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.currentOwner = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<ShipmentCreatedEventResponse> shipmentCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getShipmentCreatedEventFromLog(log));
    }

    public Flowable<ShipmentCreatedEventResponse> shipmentCreatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SHIPMENTCREATED_EVENT));
        return shipmentCreatedEventFlowable(filter);
    }

    public static List<ShipmentRetrievedEventResponse> getShipmentRetrievedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SHIPMENTRETRIEVED_EVENT, transactionReceipt);
        ArrayList<ShipmentRetrievedEventResponse> responses = new ArrayList<ShipmentRetrievedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ShipmentRetrievedEventResponse typedResponse = new ShipmentRetrievedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.origin = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.destination = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.deliveryDate = (String) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.units = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.weight = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
            typedResponse.currentState = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();
            typedResponse.currentOwner = (String) eventValues.getNonIndexedValues().get(8).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ShipmentRetrievedEventResponse getShipmentRetrievedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SHIPMENTRETRIEVED_EVENT, log);
        ShipmentRetrievedEventResponse typedResponse = new ShipmentRetrievedEventResponse();
        typedResponse.log = log;
        typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.description = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.origin = (String) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.destination = (String) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.deliveryDate = (String) eventValues.getNonIndexedValues().get(4).getValue();
        typedResponse.units = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
        typedResponse.weight = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
        typedResponse.currentState = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();
        typedResponse.currentOwner = (String) eventValues.getNonIndexedValues().get(8).getValue();
        return typedResponse;
    }

    public Flowable<ShipmentRetrievedEventResponse> shipmentRetrievedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getShipmentRetrievedEventFromLog(log));
    }

    public Flowable<ShipmentRetrievedEventResponse> shipmentRetrievedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SHIPMENTRETRIEVED_EVENT));
        return shipmentRetrievedEventFlowable(filter);
    }

    public static List<TransferCreatedEventResponse> getTransferCreatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TRANSFERCREATED_EVENT, transactionReceipt);
        ArrayList<TransferCreatedEventResponse> responses = new ArrayList<TransferCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferCreatedEventResponse typedResponse = new TransferCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.shipmentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newState = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TransferCreatedEventResponse getTransferCreatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TRANSFERCREATED_EVENT, log);
        TransferCreatedEventResponse typedResponse = new TransferCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.shipmentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newState = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<TransferCreatedEventResponse> transferCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTransferCreatedEventFromLog(log));
    }

    public Flowable<TransferCreatedEventResponse> transferCreatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFERCREATED_EVENT));
        return transferCreatedEventFlowable(filter);
    }

    public static List<TransferRetrievedEventResponse> getTransferRetrievedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(TRANSFERRETRIEVED_EVENT, transactionReceipt);
        ArrayList<TransferRetrievedEventResponse> responses = new ArrayList<TransferRetrievedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferRetrievedEventResponse typedResponse = new TransferRetrievedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.shipmentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.newState = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.location = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.newShipmentOwner = (String) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.transferNotes = (String) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static TransferRetrievedEventResponse getTransferRetrievedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TRANSFERRETRIEVED_EVENT, log);
        TransferRetrievedEventResponse typedResponse = new TransferRetrievedEventResponse();
        typedResponse.log = log;
        typedResponse.id = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.shipmentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.newState = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.location = (String) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.newShipmentOwner = (String) eventValues.getNonIndexedValues().get(4).getValue();
        typedResponse.transferNotes = (String) eventValues.getNonIndexedValues().get(5).getValue();
        return typedResponse;
    }

    public Flowable<TransferRetrievedEventResponse> transferRetrievedEventFlowable(
            EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getTransferRetrievedEventFromLog(log));
    }

    public Flowable<TransferRetrievedEventResponse> transferRetrievedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFERRETRIEVED_EVENT));
        return transferRetrievedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> createShipment(String productName,
            String description, String origin, String destination, String deliveryDate,
            BigInteger units, BigInteger weight) {
        final Function function = new Function(
                FUNC_CREATESHIPMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(productName), 
                new org.web3j.abi.datatypes.Utf8String(description), 
                new org.web3j.abi.datatypes.Utf8String(origin), 
                new org.web3j.abi.datatypes.Utf8String(destination), 
                new org.web3j.abi.datatypes.Utf8String(deliveryDate), 
                new org.web3j.abi.datatypes.generated.Uint256(units), 
                new org.web3j.abi.datatypes.generated.Uint256(weight)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getNextShipmentId() {
        final Function function = new Function(FUNC_GETNEXTSHIPMENTID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getNextTransferId() {
        final Function function = new Function(FUNC_GETNEXTTRANSFERID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> getShipment(BigInteger shipmentId) {
        final Function function = new Function(
                FUNC_GETSHIPMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(shipmentId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> getTransferByIndex(BigInteger shipmentId,
            BigInteger index) {
        final Function function = new Function(
                FUNC_GETTRANSFERBYINDEX, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(shipmentId), 
                new org.web3j.abi.datatypes.generated.Uint256(index)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getTransferCount(BigInteger shipmentId) {
        final Function function = new Function(FUNC_GETTRANSFERCOUNT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(shipmentId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> shipmentTransfer(BigInteger shipmentId,
            String newShipmentOwner, BigInteger newState, String location, String transferNotes) {
        final Function function = new Function(
                FUNC_SHIPMENTTRANSFER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(shipmentId), 
                new org.web3j.abi.datatypes.Address(160, newShipmentOwner), 
                new org.web3j.abi.datatypes.generated.Uint8(newState), 
                new org.web3j.abi.datatypes.Utf8String(location), 
                new org.web3j.abi.datatypes.Utf8String(transferNotes)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ShipmentManagement load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ShipmentManagement(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ShipmentManagement load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ShipmentManagement(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ShipmentManagement load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ShipmentManagement(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ShipmentManagement load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ShipmentManagement(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ShipmentManagement> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ShipmentManagement.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<ShipmentManagement> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ShipmentManagement.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<ShipmentManagement> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ShipmentManagement.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<ShipmentManagement> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ShipmentManagement.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class ShipmentCreatedEventResponse extends BaseEventResponse {
        public BigInteger id;

        public String deliveryDate;

        public String currentOwner;
    }

    public static class ShipmentRetrievedEventResponse extends BaseEventResponse {
        public BigInteger id;

        public String name;

        public String description;

        public String origin;

        public String destination;

        public String deliveryDate;

        public BigInteger units;

        public BigInteger weight;

        public BigInteger currentState;

        public String currentOwner;
    }

    public static class TransferCreatedEventResponse extends BaseEventResponse {
        public BigInteger shipmentId;

        public BigInteger newState;
    }

    public static class TransferRetrievedEventResponse extends BaseEventResponse {
        public BigInteger id;

        public BigInteger shipmentId;

        public BigInteger timestamp;

        public BigInteger newState;

        public String location;

        public String newShipmentOwner;

        public String transferNotes;
    }
}
