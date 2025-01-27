package smartContracts.web3;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.abi.datatypes.reflection.Parameterized;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple10;
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
    public static final String BINARY = "6080604052348015600e575f80fd5b50611c228061001c5f395ff3fe608060405234801561000f575f80fd5b5060043610610060575f3560e01c8063092b3740146100645780633fd18506146100965780636f77926b146100ab5780638bb27d02146100cb578063d3a0ace3146100eb578063f0d4e257146100fe575b5f80fd5b6100776100723660046113fe565b610111565b60405161008d9a99989796959493929190611477565b60405180910390f35b6100a96100a43660046115ef565b6104d0565b005b6100be6100b93660046116cb565b6109e3565b60405161008d9190611745565b6100de6100d93660046113fe565b610c78565b60405161008d91906117b6565b6100a96100f9366004611856565b610dd8565b6100a961010c366004611972565b6110f8565b335f90815260046020526040812054606090819081908190859081908190819085906001600160a01b03166101615760405162461bcd60e51b8152600401610158906119da565b60405180910390fd5b5f60025f8d81526020019081526020015f20604051806101400160405290815f820154815260200160018201805461019890611a08565b80601f01602080910402602001604051908101604052809291908181526020018280546101c490611a08565b801561020f5780601f106101e65761010080835404028352916020019161020f565b820191905f5260205f20905b8154815290600101906020018083116101f257829003601f168201915b5050505050815260200160028201805461022890611a08565b80601f016020809104026020016040519081016040528092919081815260200182805461025490611a08565b801561029f5780601f106102765761010080835404028352916020019161029f565b820191905f5260205f20905b81548152906001019060200180831161028257829003601f168201915b505050505081526020016003820180546102b890611a08565b80601f01602080910402602001604051908101604052809291908181526020018280546102e490611a08565b801561032f5780601f106103065761010080835404028352916020019161032f565b820191905f5260205f20905b81548152906001019060200180831161031257829003601f168201915b5050505050815260200160048201805461034890611a08565b80601f016020809104026020016040519081016040528092919081815260200182805461037490611a08565b80156103bf5780601f10610396576101008083540402835291602001916103bf565b820191905f5260205f20905b8154815290600101906020018083116103a257829003601f168201915b50505091835250506005820154602082015260068201546040820152600782015460609091019060ff1660038111156103fa576103fa611443565b600381111561040b5761040b611443565b8152600782015461010090046001600160a01b0316602080830191909152600883018054604080518285028101850182528281529401939283018282801561047057602002820191905f5260205f20905b81548152602001906001019080831161045c575b5050505050815250509050805f015181602001518260400151836060015184608001518560a001518660c001518760e001518861010001518961012001519a509a509a509a509a509a509a509a509a509a50509193959799509193959799565b604080518082018252600781526626b0b730b3b2b960c91b602080830191909152335f9081526004825283812084516080810190955280546001600160a01b0316855260018101805494959294929391929184019161052e90611a08565b80601f016020809104026020016040519081016040528092919081815260200182805461055a90611a08565b80156105a55780601f1061057c576101008083540402835291602001916105a5565b820191905f5260205f20905b81548152906001019060200180831161058857829003601f168201915b5050505050815260200160028201805480602002602001604051908101604052809291908181526020015f905b8282101561067a578382905f5260205f200180546105ef90611a08565b80601f016020809104026020016040519081016040528092919081815260200182805461061b90611a08565b80156106665780601f1061063d57610100808354040283529160200191610666565b820191905f5260205f20905b81548152906001019060200180831161064957829003601f168201915b5050505050815260200190600101906105d2565b50505050815260200160038201805461069290611a08565b80601f01602080910402602001604051908101604052809291908181526020018280546106be90611a08565b80156107095780601f106106e057610100808354040283529160200191610709565b820191905f5260205f20905b8154815290600101906020018083116106ec57829003601f168201915b5050509190925250508151919250506001600160a01b031661073d5760405162461bcd60e51b8152600401610158906119da565b5f805b8260400151518110156107ce578360405160200161075e9190611a40565b604051602081830303815290604052805190602001208360400151828151811061078a5761078a611a56565b60200260200101516040516020016107a29190611a40565b60405160208183030381529060405280519060200120036107c657600191506107ce565b600101610740565b508061081c5760405162461bcd60e51b815260206004820152601d60248201527f4e6f7420617574686f72697a656420666f72207468697320726f6c652e0000006044820152606401610158565b5f8054818061082a83611a6a565b9190505590506040518061014001604052808281526020018b81526020018a81526020018981526020018881526020018781526020018681526020015f600381111561087857610878611443565b81523360208201526040015f6040519080825280602002602001820160405280156108ad578160200160208202803683370190505b5090525f828152600260209081526040909120825181559082015160018201906108d79082611ada565b50604082015160028201906108ec9082611ada565b50606082015160038201906109019082611ada565b50608082015160048201906109169082611ada565b5060a0820151600582015560c0820151600682015560e082015160078201805460ff1916600183600381111561094e5761094e611443565b021790555061010082810151600783018054610100600160a81b0319166001600160a01b0390921690920217905561012082015180516109989160088401916020909101906112fb565b50506040513391507f8e4c701ce5fb405651e38e1b8e96ea0a29ddec2fc923811b52b9c87f1da33ef4906109cf9084908e90611b94565b60405180910390a250505050505050505050565b610a1660405180608001604052805f6001600160a01b031681526020016060815260200160608152602001606081525090565b335f908152600460205260409020546001600160a01b0316610a4a5760405162461bcd60e51b8152600401610158906119da565b6001600160a01b038083165f908152600460209081526040918290208251608081019093528054909316825260018301805492939291840191610a8c90611a08565b80601f0160208091040260200160405190810160405280929190818152602001828054610ab890611a08565b8015610b035780601f10610ada57610100808354040283529160200191610b03565b820191905f5260205f20905b815481529060010190602001808311610ae657829003601f168201915b5050505050815260200160028201805480602002602001604051908101604052809291908181526020015f905b82821015610bd8578382905f5260205f20018054610b4d90611a08565b80601f0160208091040260200160405190810160405280929190818152602001828054610b7990611a08565b8015610bc45780601f10610b9b57610100808354040283529160200191610bc4565b820191905f5260205f20905b815481529060010190602001808311610ba757829003601f168201915b505050505081526020019060010190610b30565b505050508152602001600382018054610bf090611a08565b80601f0160208091040260200160405190810160405280929190818152602001828054610c1c90611a08565b8015610c675780601f10610c3e57610100808354040283529160200191610c67565b820191905f5260205f20905b815481529060010190602001808311610c4a57829003601f168201915b50505050508152505090505b919050565b335f908152600460205260409020546060906001600160a01b0316610caf5760405162461bcd60e51b8152600401610158906119da565b5f82815260036020908152604080832080548251818502810185019093528083529193909284015b82821015610dcd575f8481526020908190206040805160a081018252600586029092018054835260018101549383019390935260028301549082015260038201546001600160a01b03166060820152600482018054919291608084019190610d3e90611a08565b80601f0160208091040260200160405190810160405280929190818152602001828054610d6a90611a08565b8015610db55780601f10610d8c57610100808354040283529160200191610db5565b820191905f5260205f20905b815481529060010190602001808311610d9857829003601f168201915b50505050508152505081526020019060010190610cd7565b505050509050919050565b335f908152600460205260409020546001600160a01b031615610e3d5760405162461bcd60e51b815260206004820152601860248201527f5573657220616c726561647920726567697374657265642e00000000000000006044820152606401610158565b5f825111610e975760405162461bcd60e51b815260206004820152602160248201527f55736572206d7573742068617665206174206c65617374206f6e6520726f6c656044820152601760f91b6064820152608401610158565b5f5b8251811015611023577f6d439300980e333f0256d64be2c9f67e86f4493ce25f82498d6db7f4be3d9e6f838281518110610ed557610ed5611a56565b6020026020010151604051602001610eed9190611a40565b604051602081830303815290604052805190602001201480610f6d57507f71b43d66088715f0148d07b45a38f510fffe2d5fd108ee7701691e554738d61b838281518110610f3d57610f3d611a56565b6020026020010151604051602001610f559190611a40565b60405160208183030381529060405280519060200120145b80610fd657507f25b0d18cf2326f98dd77bebd7e49544e2b790cdfea059f27b3335e791c24c0d7838281518110610fa657610fa6611a56565b6020026020010151604051602001610fbe9190611a40565b60405160208183030381529060405280519060200120145b61101b5760405162461bcd60e51b815260206004820152601660248201527524b73b30b634b2103937b63290383937bb34b232b21760511b6044820152606401610158565b600101610e99565b5060408051608081018252338082526020808301878152838501879052606084018690525f9283526004909152929020815181546001600160a01b0319166001600160a01b03909116178155915190919060018201906110839082611ada565b506040820151805161109f916002840191602090910190611344565b50606082015160038201906110b49082611ada565b50506040513391507fa829c8622530f92e2b272cea9a11acaf81bbd6fb95ac0e5ec4af90790f7db6cb906110eb9086908690611bb4565b60405180910390a2505050565b5f84815260026020526040902060070154849061010090046001600160a01b0316331461117f5760405162461bcd60e51b815260206004820152602f60248201527f4f6e6c79207468652063757272656e74206f776e65722063616e20706572666f60448201526e3936903a3434b99030b1ba34b7b71760891b6064820152608401610158565b5f858152600260205260409020600781018054610100600160a81b031981166101006001600160a01b0389160290811783558692916001600160a81b03191660ff199091161760018360038111156111d9576111d9611443565b0217905550600180545f91826111ee83611a6a565b909155505f888152600360208181526040808420815160a0810183528681528084018e8152429382019384526001600160a01b038e811660608401908152608084018e8152855460018082018855968b5297909920845160059098020196875591519386019390935592516002850155915193830180546001600160a01b0319169490911693909317909255915192935091600482019061128f9082611ada565b5050506008820180546001810182555f9182526020909120018190556040516001600160a01b0387169033907fe59ca647a5502871b930c7e58c9f1672aef089ff05084e0b47828427a7696d2d906112ea908b908a90611bd8565b60405180910390a350505050505050565b828054828255905f5260205f20908101928215611334579160200282015b82811115611334578251825591602001919060010190611319565b50611340929150611394565b5090565b828054828255905f5260205f20908101928215611388579160200282015b8281111561138857825182906113789082611ada565b5091602001919060010190611362565b506113409291506113a8565b5b80821115611340575f8155600101611395565b80821115611340575f6113bb82826113c4565b506001016113a8565b5080546113d090611a08565b5f825580601f106113df575050565b601f0160209004905f5260205f20908101906113fb9190611394565b50565b5f6020828403121561140e575f80fd5b5035919050565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b634e487b7160e01b5f52602160045260245ffd5b6004811061147357634e487b7160e01b5f52602160045260245ffd5b9052565b8a815261014060208201525f61149161014083018c611415565b82810360408401526114a3818c611415565b905082810360608401526114b7818b611415565b905082810360808401526114cb818a611415565b90508760a08401528660c08401526114e660e0840187611457565b6001600160a01b038516610100840152828103610120840152835180825260208086019201905f5b8181101561152c57835183526020938401939092019160010161150e565b50909e9d5050505050505050505050505050565b634e487b7160e01b5f52604160045260245ffd5b604051601f8201601f191681016001600160401b038111828210171561157c5761157c611540565b604052919050565b5f82601f830112611593575f80fd5b81356001600160401b038111156115ac576115ac611540565b6115bf601f8201601f1916602001611554565b8181528460208386010111156115d3575f80fd5b816020850160208301375f918101602001919091529392505050565b5f805f805f8060c08789031215611604575f80fd5b86356001600160401b03811115611619575f80fd5b61162589828a01611584565b96505060208701356001600160401b03811115611640575f80fd5b61164c89828a01611584565b95505060408701356001600160401b03811115611667575f80fd5b61167389828a01611584565b94505060608701356001600160401b0381111561168e575f80fd5b61169a89828a01611584565b9699959850939660808101359560a090910135945092505050565b80356001600160a01b0381168114610c73575f80fd5b5f602082840312156116db575f80fd5b6116e4826116b5565b9392505050565b5f82825180855260208501945060208160051b830101602085015f5b8381101561173957601f19858403018852611723838351611415565b6020988901989093509190910190600101611707565b50909695505050505050565b602080825282516001600160a01b031682820152820151608060408301525f9061177260a0840182611415565b90506040840151601f1984830301606085015261178f82826116eb565b9150506060840151601f198483030160808501526117ad8282611415565b95945050505050565b5f602082016020835280845180835260408501915060408160051b8601019250602086015f5b8281101561184a57603f19878603018452815180518652602081015160208701526040810151604087015260018060a01b0360608201511660608701526080810151905060a0608087015261183460a0870182611415565b95505060209384019391909101906001016117dc565b50929695505050505050565b5f805f60608486031215611868575f80fd5b83356001600160401b0381111561187d575f80fd5b61188986828701611584565b93505060208401356001600160401b038111156118a4575f80fd5b8401601f810186136118b4575f80fd5b80356001600160401b038111156118cd576118cd611540565b8060051b6118dd60208201611554565b918252602081840181019290810190898411156118f8575f80fd5b6020850192505b8383101561193d5782356001600160401b0381111561191c575f80fd5b61192b8b602083890101611584565b835250602092830192909101906118ff565b9550505050604085013590506001600160401b0381111561195c575f80fd5b61196886828701611584565b9150509250925092565b5f805f8060808587031215611985575f80fd5b84359350611995602086016116b5565b92506040850135600481106119a8575f80fd5b915060608501356001600160401b038111156119c2575f80fd5b6119ce87828801611584565b91505092959194509250565b6020808252601490820152732ab9b2b9103737ba103932b3b4b9ba32b932b21760611b604082015260600190565b600181811c90821680611a1c57607f821691505b602082108103611a3a57634e487b7160e01b5f52602260045260245ffd5b50919050565b5f82518060208501845e5f920191825250919050565b634e487b7160e01b5f52603260045260245ffd5b5f60018201611a8757634e487b7160e01b5f52601160045260245ffd5b5060010190565b601f821115611ad557805f5260205f20601f840160051c81016020851015611ab35750805b601f840160051c820191505b81811015611ad2575f8155600101611abf565b50505b505050565b81516001600160401b03811115611af357611af3611540565b611b0781611b018454611a08565b84611a8e565b6020601f821160018114611b39575f8315611b225750848201515b5f19600385901b1c1916600184901b178455611ad2565b5f84815260208120601f198516915b82811015611b685787850151825560209485019460019092019101611b48565b5084821015611b8557868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b828152604060208201525f611bac6040830184611415565b949350505050565b604081525f611bc66040830185611415565b82810360208401526117ad81856116eb565b828152604081016116e4602083018461145756fea2646970667358221220eb943520081f0fa8cb55549854c9e13248839e89b486885a2217c4827ab2b57b64736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATESHIPMENT = "createShipment";

    public static final String FUNC_GETSHIPMENT = "getShipment";

    public static final String FUNC_GETTRANSFERHISTORY = "getTransferHistory";

    public static final String FUNC_GETUSER = "getUser";

    public static final String FUNC_REGISTERUSER = "registerUser";

    public static final String FUNC_SHIPMENTTRANSFER = "shipmentTransfer";

    public static final Event SHIPMENTCREATED_EVENT = new Event("ShipmentCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event SHIPMENTTRANSFER_EVENT = new Event("ShipmentTransfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint8>() {}));
    ;

    public static final Event USERREGISTERED_EVENT = new Event("UserRegistered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<DynamicArray<Utf8String>>() {}));
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
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.shipmentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.productName = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ShipmentCreatedEventResponse getShipmentCreatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SHIPMENTCREATED_EVENT, log);
        ShipmentCreatedEventResponse typedResponse = new ShipmentCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.shipmentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.productName = (String) eventValues.getNonIndexedValues().get(1).getValue();
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

    public static List<ShipmentTransferEventResponse> getShipmentTransferEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(SHIPMENTTRANSFER_EVENT, transactionReceipt);
        ArrayList<ShipmentTransferEventResponse> responses = new ArrayList<ShipmentTransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ShipmentTransferEventResponse typedResponse = new ShipmentTransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newShipmentOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.shipmentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.newState = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static ShipmentTransferEventResponse getShipmentTransferEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(SHIPMENTTRANSFER_EVENT, log);
        ShipmentTransferEventResponse typedResponse = new ShipmentTransferEventResponse();
        typedResponse.log = log;
        typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.newShipmentOwner = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.shipmentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.newState = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<ShipmentTransferEventResponse> shipmentTransferEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getShipmentTransferEventFromLog(log));
    }

    public Flowable<ShipmentTransferEventResponse> shipmentTransferEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SHIPMENTTRANSFER_EVENT));
        return shipmentTransferEventFlowable(filter);
    }

    public static List<UserRegisteredEventResponse> getUserRegisteredEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(USERREGISTERED_EVENT, transactionReceipt);
        ArrayList<UserRegisteredEventResponse> responses = new ArrayList<UserRegisteredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UserRegisteredEventResponse typedResponse = new UserRegisteredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.userAddress = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.roles = (List<String>) ((Array) eventValues.getNonIndexedValues().get(1)).getNativeValueCopy();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static UserRegisteredEventResponse getUserRegisteredEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(USERREGISTERED_EVENT, log);
        UserRegisteredEventResponse typedResponse = new UserRegisteredEventResponse();
        typedResponse.log = log;
        typedResponse.userAddress = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.name = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.roles = (List<String>) ((Array) eventValues.getNonIndexedValues().get(1)).getNativeValueCopy();
        return typedResponse;
    }

    public Flowable<UserRegisteredEventResponse> userRegisteredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getUserRegisteredEventFromLog(log));
    }

    public Flowable<UserRegisteredEventResponse> userRegisteredEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(USERREGISTERED_EVENT));
        return userRegisteredEventFlowable(filter);
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

    public RemoteFunctionCall<Tuple10<BigInteger, String, String, String, String, BigInteger, BigInteger, BigInteger, String, List<BigInteger>>> getShipment(
            BigInteger shipmentId) {
        final Function function = new Function(FUNC_GETSHIPMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(shipmentId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint8>() {}, new TypeReference<Address>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<Tuple10<BigInteger, String, String, String, String, BigInteger, BigInteger, BigInteger, String, List<BigInteger>>>(function,
                new Callable<Tuple10<BigInteger, String, String, String, String, BigInteger, BigInteger, BigInteger, String, List<BigInteger>>>() {
                    @Override
                    public Tuple10<BigInteger, String, String, String, String, BigInteger, BigInteger, BigInteger, String, List<BigInteger>> call(
                            ) throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple10<BigInteger, String, String, String, String, BigInteger, BigInteger, BigInteger, String, List<BigInteger>>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (BigInteger) results.get(7).getValue(), 
                                (String) results.get(8).getValue(), 
                                convertToNative((List<Uint256>) results.get(9).getValue()));
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

    public RemoteFunctionCall<User> getUser(String userAddress) {
        final Function function = new Function(FUNC_GETUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, userAddress)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<User>() {}));
        return executeRemoteCallSingleValueReturn(function, User.class);
    }

    public RemoteFunctionCall<TransactionReceipt> registerUser(String name, List<String> roles,
            String email) {
        final Function function = new Function(
                FUNC_REGISTERUSER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(name), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(roles, org.web3j.abi.datatypes.Utf8String.class)), 
                new org.web3j.abi.datatypes.Utf8String(email)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public static class User extends DynamicStruct {
        public String userAddress;

        public String name;

        public List<String> roles;

        public String email;

        public User(String userAddress, String name, List<String> roles, String email) {
            super(new org.web3j.abi.datatypes.Address(160, userAddress), 
                    new org.web3j.abi.datatypes.Utf8String(name), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                            org.web3j.abi.datatypes.Utf8String.class,
                            org.web3j.abi.Utils.typeMap(roles, org.web3j.abi.datatypes.Utf8String.class)), 
                    new org.web3j.abi.datatypes.Utf8String(email));
            this.userAddress = userAddress;
            this.name = name;
            this.roles = roles;
            this.email = email;
        }

        public User(Address userAddress, Utf8String name,
                @Parameterized(type = Utf8String.class) DynamicArray<Utf8String> roles,
                Utf8String email) {
            super(userAddress, name, roles, email);
            this.userAddress = userAddress.getValue();
            this.name = name.getValue();
            this.roles = roles.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.email = email.getValue();
        }
    }

    public static class ShipmentCreatedEventResponse extends BaseEventResponse {
        public String owner;

        public BigInteger shipmentId;

        public String productName;
    }

    public static class ShipmentTransferEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newShipmentOwner;

        public BigInteger shipmentId;

        public BigInteger newState;
    }

    public static class UserRegisteredEventResponse extends BaseEventResponse {
        public String userAddress;

        public String name;

        public List<String> roles;
    }
}
