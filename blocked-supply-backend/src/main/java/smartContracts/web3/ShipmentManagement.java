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
import org.web3j.tuples.generated.Tuple5;
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
    public static final String BINARY = "608060405260015f55600180553480156016575f80fd5b50610f46806100245f395ff3fe608060405234801561000f575f80fd5b5060043610610060575f3560e01c80630475811114610064578063092b37401461007b5780633fd18506146100905780638bb27d02146100a3578063b465a0f2146100c7578063f0d4e257146100ce575b5f80fd5b6001545b6040519081526020015b60405180910390f35b61008e61008936600461094b565b6100e1565b005b61008e61009e3660046109ff565b610189565b6100b66100b136600461094b565b610364565b604051610072959493929190610b79565b5f54610068565b61008e6100dc366004610c13565b61067a565b5f60025f8381526020019081526020015f209050805f01547f831e0e8dc3ed954b099430b92578e7986428aea5da8dbbb51bfbddbce460b7af8260010183600201846003018560040186600501548760060154886007015f9054906101000a900460ff16600381111561015657610156610c88565b60078a015460405161017d989796959493929161010090046001600160a01b031690610d53565b60405180910390a25050565b5f82116101dd5760405162461bcd60e51b815260206004820152601d60248201527f556e697473206d7573742062652067726561746572207468616e20302e00000060448201526064015b60405180910390fd5b5f811161022c5760405162461bcd60e51b815260206004820152601e60248201527f576569676874206d7573742062652067726561746572207468616e20302e000060448201526064016101d4565b5f8054818061023a83610dd2565b9190505590506040518061012001604052808281526020018881526020018781526020018681526020018581526020018481526020018381526020015f600381111561028857610288610c88565b8152336020918201525f8381526002825260409020825181559082015160018201906102b49082610e42565b50604082015160028201906102c99082610e42565b50606082015160038201906102de9082610e42565b50608082015160048201906102f39082610e42565b5060a0820151600582015560c0820151600682015560e082015160078201805460ff1916600183600381111561032b5761032b610c88565b0217905550610100918201516007919091018054610100600160a81b0319166001600160a01b0390921690920217905550505050505050565b5f818152600360205260409020546060908190819081908190806001600160401b0381111561039557610395610962565b6040519080825280602002602001820160405280156103be578160200160208202803683370190505b509550806001600160401b038111156103d9576103d9610962565b604051908082528060200260200182016040528015610402578160200160208202803683370190505b509450806001600160401b0381111561041d5761041d610962565b604051908082528060200260200182016040528015610446578160200160208202803683370190505b509350806001600160401b0381111561046157610461610962565b60405190808252806020026020018201604052801561048a578160200160208202803683370190505b509250806001600160401b038111156104a5576104a5610962565b6040519080825280602002602001820160405280156104d857816020015b60608152602001906001900390816104c35790505b5091505f5b8181101561066f575f88815260036020526040812080548390811061050457610504610efc565b905f5260205f2090600502019050805f015488838151811061052857610528610efc565b602002602001018181525050806001015487838151811061054b5761054b610efc565b602002602001018181525050806002015486838151811061056e5761056e610efc565b6020908102919091010152600381015485516001600160a01b039091169086908490811061059e5761059e610efc565b60200260200101906001600160a01b031690816001600160a01b0316815250508060040180546105cd90610c9c565b80601f01602080910402602001604051908101604052809291908181526020018280546105f990610c9c565b80156106445780601f1061061b57610100808354040283529160200191610644565b820191905f5260205f20905b81548152906001019060200180831161062757829003601f168201915b505050505084838151811061065b5761065b610efc565b6020908102919091010152506001016104dd565b505091939590929450565b5f84815260026020526040902060070154849061010090046001600160a01b031633146107015760405162461bcd60e51b815260206004820152602f60248201527f4f6e6c79207468652063757272656e74206f776e65722063616e20706572666f60448201526e3936903a3434b99030b1ba34b7b71760891b60648201526084016101d4565b5f8581526002602052604081206007015460ff1690849082600381111561072a5761072a610c88565b1480156107485750600181600381111561074657610746610c88565b145b806107805750600182600381111561076257610762610c88565b1480156107805750600281600381111561077e5761077e610c88565b145b806107b85750600282600381111561079a5761079a610c88565b1480156107b8575060038160038111156107b6576107b6610c88565b145b6108045760405162461bcd60e51b815260206004820152601860248201527f496e76616c6964207374617465207472616e736974696f6e000000000000000060448201526064016101d4565b5f878152600260205260409020600781018054610100600160a81b031981166101006001600160a01b038b160290811783558892916001600160a81b03191660ff1990911617600183600381111561085e5761085e610c88565b0217905550600180545f918261087383610dd2565b91905055905060035f8a81526020019081526020015f206040518060a001604052808381526020018b81526020014281526020018a6001600160a01b0316815260200188815250908060018154018082558091505060019003905f5260205f2090600502015f909190919091505f820151815f015560208201518160010155604082015181600201556060820151816003015f6101000a8154816001600160a01b0302191690836001600160a01b03160217905550608082015181600401908161093d9190610e42565b505050505050505050505050565b5f6020828403121561095b575f80fd5b5035919050565b634e487b7160e01b5f52604160045260245ffd5b5f82601f830112610985575f80fd5b81356001600160401b0381111561099e5761099e610962565b604051601f8201601f19908116603f011681016001600160401b03811182821017156109cc576109cc610962565b6040528181528382016020018510156109e3575f80fd5b816020850160208301375f918101602001919091529392505050565b5f805f805f8060c08789031215610a14575f80fd5b86356001600160401b03811115610a29575f80fd5b610a3589828a01610976565b96505060208701356001600160401b03811115610a50575f80fd5b610a5c89828a01610976565b95505060408701356001600160401b03811115610a77575f80fd5b610a8389828a01610976565b94505060608701356001600160401b03811115610a9e575f80fd5b610aaa89828a01610976565b9699959850939660808101359560a090910135945092505050565b5f8151808452602084019350602083015f5b82811015610af5578151865260209586019590910190600101610ad7565b5093949350505050565b5f82825180855260208501945060208160051b830101602085015f5b83811015610b6d57601f19858403018852815180518085528060208301602087015e5f602082870101526020601f19601f83011686010194505050602082019150602088019750600181019050610b1b565b50909695505050505050565b60a081525f610b8b60a0830188610ac5565b8281036020840152610b9d8188610ac5565b90508281036040840152610bb18187610ac5565b8381036060850152855180825260208088019350909101905f5b81811015610bf25783516001600160a01b0316835260209384019390920191600101610bcb565b50508381036080850152610c068186610aff565b9998505050505050505050565b5f805f8060808587031215610c26575f80fd5b8435935060208501356001600160a01b0381168114610c43575f80fd5b9250604085013560048110610c56575f80fd5b915060608501356001600160401b03811115610c70575f80fd5b610c7c87828801610976565b91505092959194509250565b634e487b7160e01b5f52602160045260245ffd5b600181811c90821680610cb057607f821691505b602082108103610cce57634e487b7160e01b5f52602260045260245ffd5b50919050565b5f8154610ce081610c9c565b808552600182168015610cfa5760018114610d1657610d4a565b60ff1983166020870152602082151560051b8701019350610d4a565b845f5260205f205f5b83811015610d415781546020828a010152600182019150602081019050610d1f565b87016020019450505b50505092915050565b61010081525f610d6761010083018b610cd4565b8281036020840152610d79818b610cd4565b90508281036040840152610d8d818a610cd4565b90508281036060840152610da18189610cd4565b6080840197909752505060a081019390935260c08301919091526001600160a01b031660e090910152949350505050565b5f60018201610def57634e487b7160e01b5f52601160045260245ffd5b5060010190565b601f821115610e3d57805f5260205f20601f840160051c81016020851015610e1b5750805b601f840160051c820191505b81811015610e3a575f8155600101610e27565b50505b505050565b81516001600160401b03811115610e5b57610e5b610962565b610e6f81610e698454610c9c565b84610df6565b6020601f821160018114610ea1575f8315610e8a5750848201515b5f19600385901b1c1916600184901b178455610e3a565b5f84815260208120601f198516915b82811015610ed05787850151825560209485019460019092019101610eb0565b5084821015610eed57868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b634e487b7160e01b5f52603260045260245ffdfea264697066735822122036a34c24fa6b589fc6a5c852cf96e57907765987be48a92d603f23fb786e404d64736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATESHIPMENT = "createShipment";

    public static final String FUNC_GETNEXTSHIPMENTID = "getNextShipmentId";

    public static final String FUNC_GETNEXTTRANSFERID = "getNextTransferId";

    public static final String FUNC_GETSHIPMENT = "getShipment";

    public static final String FUNC_GETTRANSFERHISTORY = "getTransferHistory";

    public static final String FUNC_SHIPMENTTRANSFER = "shipmentTransfer";

    public static final Event SHIPMENTRETRIEVED_EVENT = new Event("ShipmentRetrieved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
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

    public RemoteFunctionCall<Tuple5<List<BigInteger>, List<BigInteger>, List<BigInteger>, List<String>, List<String>>> getTransferHistory(
            BigInteger shipmentId) {
        final Function function = new Function(FUNC_GETTRANSFERHISTORY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(shipmentId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Address>>() {}, new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<Tuple5<List<BigInteger>, List<BigInteger>, List<BigInteger>, List<String>, List<String>>>(function,
                new Callable<Tuple5<List<BigInteger>, List<BigInteger>, List<BigInteger>, List<String>, List<String>>>() {
                    @Override
                    public Tuple5<List<BigInteger>, List<BigInteger>, List<BigInteger>, List<String>, List<String>> call(
                            ) throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<List<BigInteger>, List<BigInteger>, List<BigInteger>, List<String>, List<String>>(
                                convertToNative((List<Uint256>) results.get(0).getValue()), 
                                convertToNative((List<Uint256>) results.get(1).getValue()), 
                                convertToNative((List<Uint256>) results.get(2).getValue()), 
                                convertToNative((List<Address>) results.get(3).getValue()), 
                                convertToNative((List<Utf8String>) results.get(4).getValue()));
                    }
                });
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
}
