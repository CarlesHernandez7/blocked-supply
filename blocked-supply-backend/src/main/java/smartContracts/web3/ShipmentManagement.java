package smartContracts.web3;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple9;
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
    public static final String BINARY = "608060405260015f55600180553480156016575f80fd5b50610f64806100245f395ff3fe608060405234801561000f575f80fd5b506004361061004a575f3560e01c8063092b37401461004e5780633fd185061461007f5780638bb27d0214610094578063f0d4e257146100b4575b5f80fd5b61006161005c366004610a4a565b6100c7565b60405161007699989796959493929190610aa3565b60405180910390f35b61009261008d366004610beb565b610465565b005b6100a76100a2366004610a4a565b61063b565b6040516100769190610cb5565b6100926100c2366004610d55565b61076e565b5f6060806060805f805f8060025f8b81526020019081526020015f205f01545f036101395760405162461bcd60e51b815260206004820152601760248201527f536869706d656e7420646f6573206e6f7420657869737400000000000000000060448201526064015b60405180910390fd5b5f60025f8c81526020019081526020015f20604051806101200160405290815f820154815260200160018201805461017090610dcb565b80601f016020809104026020016040519081016040528092919081815260200182805461019c90610dcb565b80156101e75780601f106101be576101008083540402835291602001916101e7565b820191905f5260205f20905b8154815290600101906020018083116101ca57829003601f168201915b5050505050815260200160028201805461020090610dcb565b80601f016020809104026020016040519081016040528092919081815260200182805461022c90610dcb565b80156102775780601f1061024e57610100808354040283529160200191610277565b820191905f5260205f20905b81548152906001019060200180831161025a57829003601f168201915b5050505050815260200160038201805461029090610dcb565b80601f01602080910402602001604051908101604052809291908181526020018280546102bc90610dcb565b80156103075780601f106102de57610100808354040283529160200191610307565b820191905f5260205f20905b8154815290600101906020018083116102ea57829003601f168201915b5050505050815260200160048201805461032090610dcb565b80601f016020809104026020016040519081016040528092919081815260200182805461034c90610dcb565b80156103975780601f1061036e57610100808354040283529160200191610397565b820191905f5260205f20905b81548152906001019060200180831161037a57829003601f168201915b50505091835250506005820154602082015260068201546040820152600782015460609091019060ff1660038111156103d2576103d2610a8f565b60038111156103e3576103e3610a8f565b81526020016007820160019054906101000a90046001600160a01b03166001600160a01b03166001600160a01b0316815250509050805f015181602001518260400151836060015184608001518560a001518660c001518760e00151886101000151995099509950995099509950995099509950509193959799909294969850565b5f82116104b45760405162461bcd60e51b815260206004820152601d60248201527f556e697473206d7573742062652067726561746572207468616e20302e0000006044820152606401610130565b5f81116105035760405162461bcd60e51b815260206004820152601e60248201527f576569676874206d7573742062652067726561746572207468616e20302e00006044820152606401610130565b5f8054818061051183610e03565b9190505590506040518061012001604052808281526020018881526020018781526020018681526020018581526020018481526020018381526020015f600381111561055f5761055f610a8f565b8152336020918201525f83815260028252604090208251815590820151600182019061058b9082610e73565b50604082015160028201906105a09082610e73565b50606082015160038201906105b59082610e73565b50608082015160048201906105ca9082610e73565b5060a0820151600582015560c0820151600682015560e082015160078201805460ff1916600183600381111561060257610602610a8f565b0217905550610100918201516007919091018054610100600160a81b0319166001600160a01b0390921690920217905550505050505050565b606060035f8381526020019081526020015f20805480602002602001604051908101604052809291908181526020015f905b82821015610763575f8481526020908190206040805160a081018252600586029092018054835260018101549383019390935260028301549082015260038201546001600160a01b031660608201526004820180549192916080840191906106d490610dcb565b80601f016020809104026020016040519081016040528092919081815260200182805461070090610dcb565b801561074b5780601f106107225761010080835404028352916020019161074b565b820191905f5260205f20905b81548152906001019060200180831161072e57829003601f168201915b5050505050815250508152602001906001019061066d565b505050509050919050565b5f84815260026020526040902060070154849061010090046001600160a01b031633146107f55760405162461bcd60e51b815260206004820152602f60248201527f4f6e6c79207468652063757272656e74206f776e65722063616e20706572666f60448201526e3936903a3434b99030b1ba34b7b71760891b6064820152608401610130565b5f8581526002602052604081206007015460ff1690849082600381111561081e5761081e610a8f565b14801561083c5750600181600381111561083a5761083a610a8f565b145b806108745750600182600381111561085657610856610a8f565b1480156108745750600281600381111561087257610872610a8f565b145b806108ac5750600282600381111561088e5761088e610a8f565b1480156108ac575060038160038111156108aa576108aa610a8f565b145b6108f85760405162461bcd60e51b815260206004820152601860248201527f496e76616c6964207374617465207472616e736974696f6e00000000000000006044820152606401610130565b5f878152600260205260409020600781018054610100600160a81b031981166101006001600160a01b038b8116820292831785559083041692899290916001600160a81b031990911660ff1990911617600183600381111561095c5761095c610a8f565b0217905550600180545f918261097183610e03565b91905055905060035f8b81526020019081526020015f206040518060a001604052808381526020018c81526020014281526020018b6001600160a01b0316815260200189815250908060018154018082558091505060019003905f5260205f2090600502015f909190919091505f820151815f015560208201518160010155604082015181600201556060820151816003015f6101000a8154816001600160a01b0302191690836001600160a01b031602179055506080820151816004019081610a3b9190610e73565b50505050505050505050505050565b5f60208284031215610a5a575f80fd5b5035919050565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b634e487b7160e01b5f52602160045260245ffd5b89815261012060208201525f610abd61012083018b610a61565b8281036040840152610acf818b610a61565b90508281036060840152610ae3818a610a61565b90508281036080840152610af78189610a61565b9150508560a08301528460c083015260048410610b2257634e487b7160e01b5f52602160045260245ffd5b8360e0830152610b3e6101008301846001600160a01b03169052565b9a9950505050505050505050565b634e487b7160e01b5f52604160045260245ffd5b5f82601f830112610b6f575f80fd5b813567ffffffffffffffff811115610b8957610b89610b4c565b604051601f8201601f19908116603f0116810167ffffffffffffffff81118282101715610bb857610bb8610b4c565b604052818152838201602001851015610bcf575f80fd5b816020850160208301375f918101602001919091529392505050565b5f805f805f8060c08789031215610c00575f80fd5b863567ffffffffffffffff811115610c16575f80fd5b610c2289828a01610b60565b965050602087013567ffffffffffffffff811115610c3e575f80fd5b610c4a89828a01610b60565b955050604087013567ffffffffffffffff811115610c66575f80fd5b610c7289828a01610b60565b945050606087013567ffffffffffffffff811115610c8e575f80fd5b610c9a89828a01610b60565b9699959850939660808101359560a090910135945092505050565b5f602082016020835280845180835260408501915060408160051b8601019250602086015f5b82811015610d4957603f19878603018452815180518652602081015160208701526040810151604087015260018060a01b0360608201511660608701526080810151905060a06080870152610d3360a0870182610a61565b9550506020938401939190910190600101610cdb565b50929695505050505050565b5f805f8060808587031215610d68575f80fd5b8435935060208501356001600160a01b0381168114610d85575f80fd5b9250604085013560048110610d98575f80fd5b9150606085013567ffffffffffffffff811115610db3575f80fd5b610dbf87828801610b60565b91505092959194509250565b600181811c90821680610ddf57607f821691505b602082108103610dfd57634e487b7160e01b5f52602260045260245ffd5b50919050565b5f60018201610e2057634e487b7160e01b5f52601160045260245ffd5b5060010190565b601f821115610e6e57805f5260205f20601f840160051c81016020851015610e4c5750805b601f840160051c820191505b81811015610e6b575f8155600101610e58565b50505b505050565b815167ffffffffffffffff811115610e8d57610e8d610b4c565b610ea181610e9b8454610dcb565b84610e27565b6020601f821160018114610ed3575f8315610ebc5750848201515b5f19600385901b1c1916600184901b178455610e6b565b5f84815260208120601f198516915b82811015610f025787850151825560209485019460019092019101610ee2565b5084821015610f1f57868401515f19600387901b60f8161c191681555b50505050600190811b0190555056fea2646970667358221220f89aad6419917cbafb6cfee89335b22e31fd566afe67cd7310d989eae5bbbf7d64736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATESHIPMENT = "createShipment";

    public static final String FUNC_GETSHIPMENT = "getShipment";

    public static final String FUNC_GETTRANSFERHISTORY = "getTransferHistory";

    public static final String FUNC_SHIPMENTTRANSFER = "shipmentTransfer";

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

    public RemoteFunctionCall<Tuple9<BigInteger, String, String, String, String, BigInteger, BigInteger, BigInteger, String>> getShipment(
            BigInteger shipmentId) {
        final Function function = new Function(FUNC_GETSHIPMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(shipmentId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}, new TypeReference<Address>() {}));
        return new RemoteFunctionCall<Tuple9<BigInteger, String, String, String, String, BigInteger, BigInteger, BigInteger, String>>(function,
                new Callable<Tuple9<BigInteger, String, String, String, String, BigInteger, BigInteger, BigInteger, String>>() {
                    @Override
                    public Tuple9<BigInteger, String, String, String, String, BigInteger, BigInteger, BigInteger, String> call(
                            ) throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple9<BigInteger, String, String, String, String, BigInteger, BigInteger, BigInteger, String>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (BigInteger) results.get(7).getValue(), 
                                (String) results.get(8).getValue());
                    }
                });
    }

    public RemoteFunctionCall<List> getTransferHistory(BigInteger shipmentId) {
        final Function function = new Function(FUNC_GETTRANSFERHISTORY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(shipmentId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Transfer>>() {}));
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

    public static class Transfer extends DynamicStruct {
        public BigInteger id;

        public BigInteger shipmentId;

        public BigInteger timestamp;

        public String newShipmentOwner;

        public String transferNotes;

        public Transfer(BigInteger id, BigInteger shipmentId, BigInteger timestamp,
                String newShipmentOwner, String transferNotes) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id), 
                    new org.web3j.abi.datatypes.generated.Uint256(shipmentId), 
                    new org.web3j.abi.datatypes.generated.Uint256(timestamp), 
                    new org.web3j.abi.datatypes.Address(160, newShipmentOwner), 
                    new org.web3j.abi.datatypes.Utf8String(transferNotes));
            this.id = id;
            this.shipmentId = shipmentId;
            this.timestamp = timestamp;
            this.newShipmentOwner = newShipmentOwner;
            this.transferNotes = transferNotes;
        }

        public Transfer(Uint256 id, Uint256 shipmentId, Uint256 timestamp, Address newShipmentOwner,
                Utf8String transferNotes) {
            super(id, shipmentId, timestamp, newShipmentOwner, transferNotes);
            this.id = id.getValue();
            this.shipmentId = shipmentId.getValue();
            this.timestamp = timestamp.getValue();
            this.newShipmentOwner = newShipmentOwner.getValue();
            this.transferNotes = transferNotes.getValue();
        }
    }
}
