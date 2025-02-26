package smartContracts.web3;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    public static final String BINARY = "608060405260015f55600180553480156016575f80fd5b50610ba7806100245f395ff3fe608060405234801561000f575f80fd5b5060043610610079575f3560e01c80633fd18506116100585780633fd18506146100cb57806398facc3b146100de578063b465a0f2146100f1578063f0d4e257146100f8575f80fd5b80623a4a0f1461007d57806304758111146100ae578063092b3740146100b6575b5f80fd5b61009c61008b366004610695565b5f9081526003602052604090205490565b60405190815260200160405180910390f35b60015461009c565b6100c96100c4366004610695565b61010b565b005b6100c96100d936600461074b565b6101b3565b6100c96100ec366004610815565b61038e565b5f5461009c565b6100c9610106366004610835565b610493565b5f60025f8381526020019081526020015f209050805f01547f831e0e8dc3ed954b099430b92578e7986428aea5da8dbbb51bfbddbce460b7af8260010183600201846003018560040186600501548760060154886007015f9054906101000a900460ff166003811115610180576101806108ab565b60078a01546040516101a7989796959493929161010090046001600160a01b031690610976565b60405180910390a25050565b5f82116102075760405162461bcd60e51b815260206004820152601d60248201527f556e697473206d7573742062652067726561746572207468616e20302e00000060448201526064015b60405180910390fd5b5f81116102565760405162461bcd60e51b815260206004820152601e60248201527f576569676874206d7573742062652067726561746572207468616e20302e000060448201526064016101fe565b5f80548180610264836109f5565b9190505590506040518061012001604052808281526020018881526020018781526020018681526020018581526020018481526020018381526020015f60038111156102b2576102b26108ab565b8152336020918201525f8381526002825260409020825181559082015160018201906102de9082610a65565b50604082015160028201906102f39082610a65565b50606082015160038201906103089082610a65565b506080820151600482019061031d9082610a65565b5060a0820151600582015560c0820151600682015560e082015160078201805460ff19166001836003811115610355576103556108ab565b0217905550610100918201516007919091018054610100600160a81b0319166001600160a01b0390921690920217905550505050505050565b5f8281526003602052604090205481106103e05760405162461bcd60e51b8152602060048201526013602482015272496e646578206f7574206f6620626f756e647360681b60448201526064016101fe565b5f8281526003602052604081208054839081106103ff576103ff610b20565b905f5260205f2090600502019050805f01547febbbd34185f1c952de9c4ad32d1b9ff8d4e8cb3461501e88890c79343849837282600101548360020154846003015f9054906101000a900460ff16600381111561045e5761045e6108ab565b60038601546040516104869493929161010090046001600160a01b0316906004890190610b34565b60405180910390a2505050565b5f84815260026020526040902060070154849061010090046001600160a01b0316331461051a5760405162461bcd60e51b815260206004820152602f60248201527f4f6e6c79207468652063757272656e74206f776e65722063616e20706572666f60448201526e3936903a3434b99030b1ba34b7b71760891b60648201526084016101fe565b5f858152600260205260409020600781018054610100600160a81b031981166101006001600160a01b0389160290811783558692916001600160a81b03191660ff19909116176001836003811115610574576105746108ab565b0217905550600180545f9182610589836109f5565b91905055905060035f8881526020019081526020015f206040518060c001604052808381526020018981526020014281526020018760038111156105cf576105cf6108ab565b81526001600160a01b03891660208083019190915260409182018890528354600181810186555f9586529482902084516005909202019081559083015181850155908201516002820155606082015160038083018054949593949293909260ff1916918490811115610643576106436108ab565b021790555060808201516003820180546001600160a01b0390921661010002610100600160a81b031990921691909117905560a082015160048201906106899082610a65565b50505050505050505050565b5f602082840312156106a5575f80fd5b5035919050565b634e487b7160e01b5f52604160045260245ffd5b5f82601f8301126106cf575f80fd5b813567ffffffffffffffff8111156106e9576106e96106ac565b604051601f8201601f19908116603f0116810167ffffffffffffffff81118282101715610718576107186106ac565b60405281815283820160200185101561072f575f80fd5b816020850160208301375f918101602001919091529392505050565b5f805f805f8060c08789031215610760575f80fd5b863567ffffffffffffffff811115610776575f80fd5b61078289828a016106c0565b965050602087013567ffffffffffffffff81111561079e575f80fd5b6107aa89828a016106c0565b955050604087013567ffffffffffffffff8111156107c6575f80fd5b6107d289828a016106c0565b945050606087013567ffffffffffffffff8111156107ee575f80fd5b6107fa89828a016106c0565b9699959850939660808101359560a090910135945092505050565b5f8060408385031215610826575f80fd5b50508035926020909101359150565b5f805f8060808587031215610848575f80fd5b8435935060208501356001600160a01b0381168114610865575f80fd5b9250604085013560048110610878575f80fd5b9150606085013567ffffffffffffffff811115610893575f80fd5b61089f878288016106c0565b91505092959194509250565b634e487b7160e01b5f52602160045260245ffd5b600181811c908216806108d357607f821691505b6020821081036108f157634e487b7160e01b5f52602260045260245ffd5b50919050565b5f8154610903816108bf565b80855260018216801561091d57600181146109395761096d565b60ff1983166020870152602082151560051b870101935061096d565b845f5260205f205f5b838110156109645781546020828a010152600182019150602081019050610942565b87016020019450505b50505092915050565b61010081525f61098a61010083018b6108f7565b828103602084015261099c818b6108f7565b905082810360408401526109b0818a6108f7565b905082810360608401526109c481896108f7565b6080840197909752505060a081019390935260c08301919091526001600160a01b031660e090910152949350505050565b5f60018201610a1257634e487b7160e01b5f52601160045260245ffd5b5060010190565b601f821115610a6057805f5260205f20601f840160051c81016020851015610a3e5750805b601f840160051c820191505b81811015610a5d575f8155600101610a4a565b50505b505050565b815167ffffffffffffffff811115610a7f57610a7f6106ac565b610a9381610a8d84546108bf565b84610a19565b6020601f821160018114610ac5575f8315610aae5750848201515b5f19600385901b1c1916600184901b178455610a5d565b5f84815260208120601f198516915b82811015610af45787850151825560209485019460019092019101610ad4565b5084821015610b1157868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b634e487b7160e01b5f52603260045260245ffd5b85815284602082015283604082015260018060a01b038316606082015260a060808201525f610b6660a08301846108f7565b97965050505050505056fea264697066735822122017dd732d4db9bc8fe4c8b8d154aff3d0092603e822d2a8bf6af0965927210afd64736f6c634300081a0033";
    public static final String FUNC_CREATESHIPMENT = "createShipment";
    public static final String FUNC_GETNEXTSHIPMENTID = "getNextShipmentId";
    public static final String FUNC_GETNEXTTRANSFERID = "getNextTransferId";
    public static final String FUNC_GETSHIPMENT = "getShipment";
    public static final String FUNC_GETTRANSFERBYINDEX = "getTransferByIndex";
    public static final String FUNC_GETTRANSFERCOUNT = "getTransferCount";
    public static final String FUNC_SHIPMENTTRANSFER = "shipmentTransfer";
    public static final Event SHIPMENTRETRIEVED_EVENT = new Event("ShipmentRetrieved",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {
            }, new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }, new TypeReference<Utf8String>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Address>() {
            }));
    public static final Event TRANSFERRETRIEVED_EVENT = new Event("TransferRetrieved",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Uint256>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Utf8String>() {
            }));
    ;
    private static String librariesLinkedBinary;
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
            typedResponse.units = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.weight = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.currentState = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
            typedResponse.currentOwner = (String) eventValues.getNonIndexedValues().get(7).getValue();
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
        typedResponse.units = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
        typedResponse.weight = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
        typedResponse.currentState = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
        typedResponse.currentOwner = (String) eventValues.getNonIndexedValues().get(7).getValue();
        return typedResponse;
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
            typedResponse.newShipmentOwner = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.transferNotes = (String) eventValues.getNonIndexedValues().get(4).getValue();
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
        typedResponse.newShipmentOwner = (String) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.transferNotes = (String) eventValues.getNonIndexedValues().get(4).getValue();
        return typedResponse;
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
                                                                 String description, String origin, String destination, BigInteger units,
                                                                 BigInteger weight) {
        final Function function = new Function(
                FUNC_CREATESHIPMENT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(productName),
                        new org.web3j.abi.datatypes.Utf8String(description),
                        new org.web3j.abi.datatypes.Utf8String(origin),
                        new org.web3j.abi.datatypes.Utf8String(destination),
                        new org.web3j.abi.datatypes.generated.Uint256(units),
                        new org.web3j.abi.datatypes.generated.Uint256(weight)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> getNextShipmentId() {
        final Function function = new Function(FUNC_GETNEXTSHIPMENTID,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getNextTransferId() {
        final Function function = new Function(FUNC_GETNEXTTRANSFERID,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
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
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> shipmentTransfer(BigInteger shipmentId,
                                                                   String newShipmentOwner, BigInteger newState, String transferNotes) {
        final Function function = new Function(
                FUNC_SHIPMENTTRANSFER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(shipmentId),
                        new org.web3j.abi.datatypes.Address(160, newShipmentOwner),
                        new org.web3j.abi.datatypes.generated.Uint8(newState),
                        new org.web3j.abi.datatypes.Utf8String(transferNotes)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static class ShipmentRetrievedEventResponse extends BaseEventResponse {
        public BigInteger id;

        public String name;

        public String description;

        public String origin;

        public String destination;

        public BigInteger units;

        public BigInteger weight;

        public BigInteger currentState;

        public String currentOwner;
    }

    public static class TransferRetrievedEventResponse extends BaseEventResponse {
        public BigInteger id;

        public BigInteger shipmentId;

        public BigInteger timestamp;

        public BigInteger newState;

        public String newShipmentOwner;

        public String transferNotes;
    }
}
