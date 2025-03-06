package smartContracts.web3;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
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
    public static final String BINARY = "608060405260015f55600180553480156016575f80fd5b50610de5806100245f395ff3fe608060405234801561000f575f80fd5b5060043610610084575f3560e01c806398facc3b1161005857806398facc3b146100de5780639eb68591146100f1578063a896fe4514610111578063b465a0f214610124575f80fd5b80623a4a0f1461008857806304758111146100ae578063092b3740146100b65780633fd18506146100cb575b5f80fd5b61009b6100963660046107e7565b61012b565b6040519081526020015b60405180910390f35b60015461009b565b6100c96100c43660046107e7565b6101b7565b005b6100c96100d936600461089d565b6102cb565b6100c96100ec366004610967565b6104bf565b6101046100ff3660046109a2565b610567565b6040516100a591906109c2565b6100c961011f366004610a04565b6105d0565b5f5461009b565b5f815f81116101555760405162461bcd60e51b815260040161014c90610a97565b60405180910390fd5b5f5481106101a05760405162461bcd60e51b815260206004820152601860248201527729b434b836b2b73a103237b2b9903737ba1032bc34b9ba1760411b604482015260640161014c565b5f8381526003602052604090205491505b50919050565b805f81116101d75760405162461bcd60e51b815260040161014c90610a97565b5f5481106102225760405162461bcd60e51b815260206004820152601860248201527729b434b836b2b73a103237b2b9903737ba1032bc34b9ba1760411b604482015260640161014c565b5f60025f8481526020019081526020015f209050805f01547f831e0e8dc3ed954b099430b92578e7986428aea5da8dbbb51bfbddbce460b7af8260010183600201846003018560040186600501548760060154886007015f9054906101000a900460ff16600381111561029757610297610ada565b60078a01546040516102be989796959493929161010090046001600160a01b031690610b9f565b60405180910390a2505050565b5f821161031a5760405162461bcd60e51b815260206004820152601d60248201527f556e697473206d7573742062652067726561746572207468616e20302e000000604482015260640161014c565b5f81116103695760405162461bcd60e51b815260206004820152601e60248201527f576569676874206d7573742062652067726561746572207468616e20302e0000604482015260640161014c565b5f8054818061037783610c1e565b9190505590506040518061012001604052808281526020018881526020018781526020018681526020018581526020018481526020018381526020015f60038111156103c5576103c5610ada565b8152336020918201525f8381526002825260409020825181559082015160018201906103f19082610c8e565b50604082015160028201906104069082610c8e565b506060820151600382019061041b9082610c8e565b50608082015160048201906104309082610c8e565b5060a0820151600582015560c0820151600682015560e082015160078201805460ff1916600183600381111561046857610468610ada565b0217905550610100918201516007919091018054610100600160a81b0319166001600160a01b03909216909202179055335f9081526004602090815260408220805460018101825590835291200155505050505050565b5f8281526003602052604081208054839081106104de576104de610d49565b905f5260205f2090600702019050805f01547fcd3a3f2736fc1d326c90624a647d8290aaa56c1bc81375e79ca971e40da12d6582600101548360020154846003015f9054906101000a900460ff16600381111561053d5761053d610ada565b60058601546040516102be9493929160048901916001600160a01b039091169060068a0190610d5d565b6001600160a01b0381165f908152600460209081526040918290208054835181840281018401909452808452606093928301828280156105c457602002820191905f5260205f20905b8154815260200190600101908083116105b0575b50505050509050919050565b5f85815260026020526040902060070154859061010090046001600160a01b031633146106575760405162461bcd60e51b815260206004820152602f60248201527f4f6e6c79207468652063757272656e74206f776e65722063616e20706572666f60448201526e3936903a3434b99030b1ba34b7b71760891b606482015260840161014c565b5f868152600260205260409020600781018054610100600160a81b031981166101006001600160a01b038a160290811783558792916001600160a81b03191660ff199091161760018360038111156106b1576106b1610ada565b0217905550600180545f91826106c683610c1e565b91905055905060035f8981526020019081526020015f206040518060e001604052808381526020018a815260200142815260200188600381111561070c5761070c610ada565b815260208082018990526001600160a01b038b1660408084019190915260609283018990528454600181810187555f968752958390208551600790920201908155918401518286015583015160028201559082015160038083018054949593949293909260ff191691849081111561078657610786610ada565b02179055506080820151600482019061079f9082610c8e565b5060a08201516005820180546001600160a01b0319166001600160a01b0390921691909117905560c082015160068201906107da9082610c8e565b5050505050505050505050565b5f602082840312156107f7575f80fd5b5035919050565b634e487b7160e01b5f52604160045260245ffd5b5f82601f830112610821575f80fd5b813567ffffffffffffffff81111561083b5761083b6107fe565b604051601f8201601f19908116603f0116810167ffffffffffffffff8111828210171561086a5761086a6107fe565b604052818152838201602001851015610881575f80fd5b816020850160208301375f918101602001919091529392505050565b5f805f805f8060c087890312156108b2575f80fd5b863567ffffffffffffffff8111156108c8575f80fd5b6108d489828a01610812565b965050602087013567ffffffffffffffff8111156108f0575f80fd5b6108fc89828a01610812565b955050604087013567ffffffffffffffff811115610918575f80fd5b61092489828a01610812565b945050606087013567ffffffffffffffff811115610940575f80fd5b61094c89828a01610812565b9699959850939660808101359560a090910135945092505050565b5f8060408385031215610978575f80fd5b50508035926020909101359150565b80356001600160a01b038116811461099d575f80fd5b919050565b5f602082840312156109b2575f80fd5b6109bb82610987565b9392505050565b602080825282518282018190525f918401906040840190835b818110156109f95783518352602093840193909201916001016109db565b509095945050505050565b5f805f805f60a08688031215610a18575f80fd5b85359450610a2860208701610987565b9350604086013560048110610a3b575f80fd5b9250606086013567ffffffffffffffff811115610a56575f80fd5b610a6288828901610812565b925050608086013567ffffffffffffffff811115610a7e575f80fd5b610a8a88828901610812565b9150509295509295909350565b60208082526023908201527f536869706d656e74204944206d7573742062652067726561746572207468616e60408201526210181760e91b606082015260800190565b634e487b7160e01b5f52602160045260245ffd5b600181811c90821680610b0257607f821691505b6020821081036101b157634e487b7160e01b5f52602260045260245ffd5b5f8154610b2c81610aee565b808552600182168015610b465760018114610b6257610b96565b60ff1983166020870152602082151560051b8701019350610b96565b845f5260205f205f5b83811015610b8d5781546020828a010152600182019150602081019050610b6b565b87016020019450505b50505092915050565b61010081525f610bb361010083018b610b20565b8281036020840152610bc5818b610b20565b90508281036040840152610bd9818a610b20565b90508281036060840152610bed8189610b20565b6080840197909752505060a081019390935260c08301919091526001600160a01b031660e090910152949350505050565b5f60018201610c3b57634e487b7160e01b5f52601160045260245ffd5b5060010190565b601f821115610c8957805f5260205f20601f840160051c81016020851015610c675750805b601f840160051c820191505b81811015610c86575f8155600101610c73565b50505b505050565b815167ffffffffffffffff811115610ca857610ca86107fe565b610cbc81610cb68454610aee565b84610c42565b6020601f821160018114610cee575f8315610cd75750848201515b5f19600385901b1c1916600184901b178455610c86565b5f84815260208120601f198516915b82811015610d1d5787850151825560209485019460019092019101610cfd565b5084821015610d3a57868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b634e487b7160e01b5f52603260045260245ffd5b86815285602082015284604082015260c060608201525f610d8160c0830186610b20565b6001600160a01b038516608084015282810360a0840152610da28185610b20565b999850505050505050505056fea264697066735822122096b96598db16c2f184faad00096d09af644f3fb66265488aa0fbc8f0195224ce64736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATESHIPMENT = "createShipment";

    public static final String FUNC_GETNEXTSHIPMENTID = "getNextShipmentId";

    public static final String FUNC_GETNEXTTRANSFERID = "getNextTransferId";

    public static final String FUNC_GETSHIPMENT = "getShipment";

    public static final String FUNC_GETSHIPMENTSBYOWNER = "getShipmentsByOwner";

    public static final String FUNC_GETTRANSFERBYINDEX = "getTransferByIndex";

    public static final String FUNC_GETTRANSFERCOUNT = "getTransferCount";

    public static final String FUNC_SHIPMENTTRANSFER = "shipmentTransfer";

    public static final Event SHIPMENTRETRIEVED_EVENT = new Event("ShipmentRetrieved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
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

    public RemoteFunctionCall<List> getShipmentsByOwner(String owner) {
        final Function function = new Function(FUNC_GETSHIPMENTSBYOWNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
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

        public String location;

        public String newShipmentOwner;

        public String transferNotes;
    }
}
