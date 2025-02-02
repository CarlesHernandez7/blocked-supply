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
    public static final String BINARY = "60076101009081526610d4915055115160ca1b610120526080908152600a61014090815269125397d514905394d25560b21b6101605260a05260066101809081526514d513d4915160d21b6101a05260c05261020060405260096101c09081526811115312559154915160ba1b6101e05260e052610080905f906004610132565b506040805160c0810182526005608082019081526420a226a4a760d91b60a0830152815281518083018352600881526721aaa9aa27a6a2a960c11b6020828101919091528083019190915282518084018452600b81526a2a2920a729a827a92a22a960a91b81830152828401528251808401909352600983526857415245484f55534560b81b908301526060810191909152610120906001906004610132565b5034801561012c575f80fd5b50610342565b828054828255905f5260205f20908101928215610176579160200282015b8281111561017657825182906101669082610288565b5091602001919060010190610150565b50610182929150610186565b5090565b80821115610182575f61019982826101a2565b50600101610186565b5080546101ae90610204565b5f825580601f106101bd575050565b601f0160209004905f5260205f20908101906101d991906101dc565b50565b5b80821115610182575f81556001016101dd565b634e487b7160e01b5f52604160045260245ffd5b600181811c9082168061021857607f821691505b60208210810361023657634e487b7160e01b5f52602260045260245ffd5b50919050565b601f82111561028357805f5260205f20601f840160051c810160208510156102615750805b601f840160051c820191505b81811015610280575f815560010161026d565b50505b505050565b81516001600160401b038111156102a1576102a16101f0565b6102b5816102af8454610204565b8461023c565b6020601f8211600181146102e7575f83156102d05750848201515b5f19600385901b1c1916600184901b178455610280565b5f84815260208120601f198516915b8281101561031657878501518255602094850194600190920191016102f6565b508482101561033357868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b61208c8061034f5f395ff3fe608060405234801561000f575f80fd5b5060043610610060575f3560e01c8063092b3740146100645780633fd185061461008d5780636f77926b146100a25780638bb27d02146100c2578063c00afe2d146100e2578063d3a0ace3146100f5575b5f80fd5b6100776100723660046116b1565b610108565b6040516100849190611730565b60405180910390f35b6100a061009b3660046118d8565b61065d565b005b6100b56100b03660046119b9565b6109c0565b60405161008491906119d9565b6100d56100d03660046116b1565b610d91565b6040516100849190611a8e565b6100a06100f0366004611b2e565b61103c565b6100a0610103366004611baa565b611319565b6101636040518061014001604052805f8152602001606081526020016060815260200160608152602001606081526020015f81526020015f8152602001606081526020015f6001600160a01b03168152602001606081525090565b604080518082018252600581526420a226a4a760d91b602080830191909152335f90815260069091529190912080546001600160a01b03166101c05760405162461bcd60e51b81526004016101b790611cc6565b60405180910390fd5b5f805b60028301548110156102c557836040516020016101e09190611cf4565b6040516020818303038152906040528051906020012083600201828154811061020b5761020b611d0a565b905f5260205f20016040516020016102239190611d56565b6040516020818303038152906040528051906020012014806102af57506040516420a226a4a760d91b60208201526025016040516020818303038152906040528051906020012083600201828154811061027f5761027f611d0a565b905f5260205f20016040516020016102979190611d56565b60405160208183030381529060405280519060200120145b156102bd57600191506102c5565b6001016101c3565b50806102e35760405162461bcd60e51b81526004016101b790611dc7565b60045f8681526020019081526020015f20604051806101400160405290815f820154815260200160018201805461031990611d1e565b80601f016020809104026020016040519081016040528092919081815260200182805461034590611d1e565b80156103905780601f1061036757610100808354040283529160200191610390565b820191905f5260205f20905b81548152906001019060200180831161037357829003601f168201915b505050505081526020016002820180546103a990611d1e565b80601f01602080910402602001604051908101604052809291908181526020018280546103d590611d1e565b80156104205780601f106103f757610100808354040283529160200191610420565b820191905f5260205f20905b81548152906001019060200180831161040357829003601f168201915b5050505050815260200160038201805461043990611d1e565b80601f016020809104026020016040519081016040528092919081815260200182805461046590611d1e565b80156104b05780601f10610487576101008083540402835291602001916104b0565b820191905f5260205f20905b81548152906001019060200180831161049357829003601f168201915b505050505081526020016004820180546104c990611d1e565b80601f01602080910402602001604051908101604052809291908181526020018280546104f590611d1e565b80156105405780601f1061051757610100808354040283529160200191610540565b820191905f5260205f20905b81548152906001019060200180831161052357829003601f168201915b50505050508152602001600582015481526020016006820154815260200160078201805461056d90611d1e565b80601f016020809104026020016040519081016040528092919081815260200182805461059990611d1e565b80156105e45780601f106105bb576101008083540402835291602001916105e4565b820191905f5260205f20905b8154815290600101906020018083116105c757829003601f168201915b505050918352505060088201546001600160a01b0316602080830191909152600983018054604080518285028101850182528281529401939283018282801561064a57602002820191905f5260205f20905b815481526020019060010190808311610636575b5050505050815250509350505050919050565b604080518082018252600581526420a226a4a760d91b602080830191909152335f90815260069091529190912080546001600160a01b03166106b15760405162461bcd60e51b81526004016101b790611cc6565b5f805b60028301548110156107b657836040516020016106d19190611cf4565b604051602081830303815290604052805190602001208360020182815481106106fc576106fc611d0a565b905f5260205f20016040516020016107149190611d56565b6040516020818303038152906040528051906020012014806107a057506040516420a226a4a760d91b60208201526025016040516020818303038152906040528051906020012083600201828154811061077057610770611d0a565b905f5260205f20016040516020016107889190611d56565b60405160208183030381529060405280519060200120145b156107ae57600191506107b6565b6001016106b4565b50806107d45760405162461bcd60e51b81526004016101b790611dc7565b600280545f91826107e483611dfe565b9190505590506040518061014001604052808281526020018b81526020018a81526020018981526020018881526020018781526020018681526020016040518060400160405280600781526020016610d4915055115160ca1b8152508152602001336001600160a01b031681526020015f6001600160401b0381111561086c5761086c611829565b604051908082528060200260200182016040528015610895578160200160208202803683370190505b5090525f828152600460209081526040909120825181559082015160018201906108bf9082611e6e565b50604082015160028201906108d49082611e6e565b50606082015160038201906108e99082611e6e565b50608082015160048201906108fe9082611e6e565b5060a0820151600582015560c0820151600682015560e082015160078201906109279082611e6e565b506101008201516008820180546001600160a01b0319166001600160a01b03909216919091179055610120820151805161096b9160098401916020909101906115ae565b50506040513391507f6e4aba77af1b5f8491619072c6ce5c2f2913827ce84ff883e18d40e6a43e3cfc906109ac9084908e908e908e908e908e908e90611f28565b60405180910390a250505050505050505050565b6109f360405180608001604052805f6001600160a01b031681526020016060815260200160608152602001606081525090565b604080518082018252600581526420a226a4a760d91b602080830191909152335f90815260069091529190912080546001600160a01b0316610a475760405162461bcd60e51b81526004016101b790611cc6565b5f805b6002830154811015610b4c5783604051602001610a679190611cf4565b60405160208183030381529060405280519060200120836002018281548110610a9257610a92611d0a565b905f5260205f2001604051602001610aaa9190611d56565b604051602081830303815290604052805190602001201480610b3657506040516420a226a4a760d91b602082015260250160405160208183030381529060405280519060200120836002018281548110610b0657610b06611d0a565b905f5260205f2001604051602001610b1e9190611d56565b60405160208183030381529060405280519060200120145b15610b445760019150610b4c565b600101610a4a565b5080610b6a5760405162461bcd60e51b81526004016101b790611dc7565b6001600160a01b038086165f908152600660209081526040918290208251608081019093528054909316825260018301805492939291840191610bac90611d1e565b80601f0160208091040260200160405190810160405280929190818152602001828054610bd890611d1e565b8015610c235780601f10610bfa57610100808354040283529160200191610c23565b820191905f5260205f20905b815481529060010190602001808311610c0657829003601f168201915b5050505050815260200160028201805480602002602001604051908101604052809291908181526020015f905b82821015610cf8578382905f5260205f20018054610c6d90611d1e565b80601f0160208091040260200160405190810160405280929190818152602001828054610c9990611d1e565b8015610ce45780601f10610cbb57610100808354040283529160200191610ce4565b820191905f5260205f20905b815481529060010190602001808311610cc757829003601f168201915b505050505081526020019060010190610c50565b505050508152602001600382018054610d1090611d1e565b80601f0160208091040260200160405190810160405280929190818152602001828054610d3c90611d1e565b801561064a5780601f10610d5e5761010080835404028352916020019161064a565b820191905f5260205f20905b815481529060010190602001808311610d6a57505050919092525091979650505050505050565b604080518082018252600881526721aaa9aa27a6a2a960c11b602080830191909152335f908152600690915291909120805460609291906001600160a01b0316610ded5760405162461bcd60e51b81526004016101b790611cc6565b5f805b6002830154811015610ef25783604051602001610e0d9190611cf4565b60405160208183030381529060405280519060200120836002018281548110610e3857610e38611d0a565b905f5260205f2001604051602001610e509190611d56565b604051602081830303815290604052805190602001201480610edc57506040516420a226a4a760d91b602082015260250160405160208183030381529060405280519060200120836002018281548110610eac57610eac611d0a565b905f5260205f2001604051602001610ec49190611d56565b60405160208183030381529060405280519060200120145b15610eea5760019150610ef2565b600101610df0565b5080610f105760405162461bcd60e51b81526004016101b790611dc7565b5f85815260056020908152604080832080548251818502810185019093528083529193909284015b8282101561102e575f8481526020908190206040805160a081018252600586029092018054835260018101549383019390935260028301549082015260038201546001600160a01b03166060820152600482018054919291608084019190610f9f90611d1e565b80601f0160208091040260200160405190810160405280929190818152602001828054610fcb90611d1e565b80156110165780601f10610fed57610100808354040283529160200191611016565b820191905f5260205f20905b815481529060010190602001808311610ff957829003601f168201915b50505050508152505081526020019060010190610f38565b505050509350505050919050565b5f8481526004602052604090206008015484906001600160a01b031633146110be5760405162461bcd60e51b815260206004820152602f60248201527f4f6e6c79207468652063757272656e74206f776e65722063616e20706572666f60448201526e3936903a3434b99030b1ba34b7b71760891b60648201526084016101b7565b5f805b5f54811015611147575f81815481106110dc576110dc611d0a565b905f5260205f20016040516020016110f49190611d56565b604051602081830303815290604052805190602001208560405160200161111b9190611cf4565b604051602081830303815290604052805190602001200361113f5760019150611147565b6001016110c1565b50806111955760405162461bcd60e51b815260206004820152601760248201527f496e76616c696420736869706d656e742073746174652e00000000000000000060448201526064016101b7565b5f8681526004602052604090206008810180546001600160a01b0319166001600160a01b038816179055600781016111cd8682611e6e565b50600380545f91826111de83611dfe565b91905055905060055f8981526020019081526020015f206040518060a001604052808381526020018a8152602001428152602001896001600160a01b0316815260200187815250908060018154018082558091505060019003905f5260205f2090600502015f909190919091505f820151815f015560208201518160010155604082015181600201556060820151816003015f6101000a8154816001600160a01b0302191690836001600160a01b0316021790555060808201518160040190816112a89190611e6e565b5050506009820180546001810182555f9182526020909120018190556040516001600160a01b0388169033907fd69adf76a589dee5f11fc53daa70309b4fcfe5b050845746bfadf73c7793c36090611307908c9042908c908c90611f91565b60405180910390a35050505050505050565b335f908152600660205260409020546001600160a01b03161561137e5760405162461bcd60e51b815260206004820152601860248201527f5573657220616c726561647920726567697374657265642e000000000000000060448201526064016101b7565b5f8251116113d85760405162461bcd60e51b815260206004820152602160248201527f55736572206d7573742068617665206174206c65617374206f6e6520726f6c656044820152601760f91b60648201526084016101b7565b5f805b8351811015611490575f5b600154811015611487576001818154811061140357611403611d0a565b905f5260205f200160405160200161141b9190611d56565b6040516020818303038152906040528051906020012085838151811061144357611443611d0a565b602002602001015160405160200161145b9190611cf4565b604051602081830303815290604052805190602001200361147f5760019250611487565b6001016113e6565b506001016113db565b50806114d75760405162461bcd60e51b815260206004820152601660248201527524b73b30b634b2103937b63290383937bb34b232b21760511b60448201526064016101b7565b60408051608081018252338082526020808301888152838501889052606084018790525f9283526006909152929020815181546001600160a01b0319166001600160a01b03909116178155915190919060018201906115369082611e6e565b50604082015180516115529160028401916020909101906115f7565b50606082015160038201906115679082611e6e565b50506040513391507f9dca0e96f2bb072d23c844aaab3f8a25a4c83051d5a70d2ec693152c580829d4906115a090879086908890611fcc565b60405180910390a250505050565b828054828255905f5260205f209081019282156115e7579160200282015b828111156115e75782518255916020019190600101906115cc565b506115f3929150611647565b5090565b828054828255905f5260205f2090810192821561163b579160200282015b8281111561163b578251829061162b9082611e6e565b5091602001919060010190611615565b506115f392915061165b565b5b808211156115f3575f8155600101611648565b808211156115f3575f61166e8282611677565b5060010161165b565b50805461168390611d1e565b5f825580601f10611692575050565b601f0160209004905f5260205f20908101906116ae9190611647565b50565b5f602082840312156116c1575f80fd5b5035919050565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b5f8151808452602084019350602083015f5b82811015611726578151865260209586019590910190600101611708565b5093949350505050565b60208152815160208201525f602083015161014060408401526117576101608401826116c8565b90506040840151601f1984830301606085015261177482826116c8565b9150506060840151601f1984830301608085015261179282826116c8565b9150506080840151601f198483030160a08501526117b082826116c8565b91505060a084015160c084015260c084015160e084015260e0840151601f19848303016101008501526117e382826116c8565b9150506101008401516118026101208501826001600160a01b03169052565b50610120840151838203601f190161014085015261182082826116f6565b95945050505050565b634e487b7160e01b5f52604160045260245ffd5b604051601f8201601f191681016001600160401b038111828210171561186557611865611829565b604052919050565b5f82601f83011261187c575f80fd5b81356001600160401b0381111561189557611895611829565b6118a8601f8201601f191660200161183d565b8181528460208386010111156118bc575f80fd5b816020850160208301375f918101602001919091529392505050565b5f805f805f8060c087890312156118ed575f80fd5b86356001600160401b03811115611902575f80fd5b61190e89828a0161186d565b96505060208701356001600160401b03811115611929575f80fd5b61193589828a0161186d565b95505060408701356001600160401b03811115611950575f80fd5b61195c89828a0161186d565b94505060608701356001600160401b03811115611977575f80fd5b61198389828a0161186d565b9699959850939660808101359560a090910135945092505050565b80356001600160a01b03811681146119b4575f80fd5b919050565b5f602082840312156119c9575f80fd5b6119d28261199e565b9392505050565b602080825282516001600160a01b031682820152820151608060408301525f90611a0660a08401826116c8565b6040850151848203601f19016060860152805180835291925060209081019181840191600582901b8501015f5b82811015611a6457601f19868303018452611a4f8286516116c8565b60209586019594909401939150600101611a33565b506060880151878203601f190160808901529450611a8281866116c8565b98975050505050505050565b5f602082016020835280845180835260408501915060408160051b8601019250602086015f5b82811015611b2257603f19878603018452815180518652602081015160208701526040810151604087015260018060a01b0360608201511660608701526080810151905060a06080870152611b0c60a08701826116c8565b9550506020938401939190910190600101611ab4565b50929695505050505050565b5f805f8060808587031215611b41575f80fd5b84359350611b516020860161199e565b925060408501356001600160401b03811115611b6b575f80fd5b611b778782880161186d565b92505060608501356001600160401b03811115611b92575f80fd5b611b9e8782880161186d565b91505092959194509250565b5f805f60608486031215611bbc575f80fd5b83356001600160401b03811115611bd1575f80fd5b611bdd8682870161186d565b93505060208401356001600160401b03811115611bf8575f80fd5b8401601f81018613611c08575f80fd5b80356001600160401b03811115611c2157611c21611829565b8060051b611c316020820161183d565b91825260208184018101929081019089841115611c4c575f80fd5b6020850192505b83831015611c915782356001600160401b03811115611c70575f80fd5b611c7f8b60208389010161186d565b83525060209283019290910190611c53565b9550505050604085013590506001600160401b03811115611cb0575f80fd5b611cbc8682870161186d565b9150509250925092565b6020808252601490820152732ab9b2b9103737ba103932b3b4b9ba32b932b21760611b604082015260600190565b5f82518060208501845e5f920191825250919050565b634e487b7160e01b5f52603260045260245ffd5b600181811c90821680611d3257607f821691505b602082108103611d5057634e487b7160e01b5f52602260045260245ffd5b50919050565b5f808354611d6381611d1e565b600182168015611d7a5760018114611d8f57611dbc565b60ff1983168652811515820286019350611dbc565b865f5260205f205f5b83811015611db457815488820152600190910190602001611d98565b505081860193505b509195945050505050565b6020808252601d908201527f4e6f7420617574686f72697a656420666f72207468697320726f6c652e000000604082015260600190565b5f60018201611e1b57634e487b7160e01b5f52601160045260245ffd5b5060010190565b601f821115611e6957805f5260205f20601f840160051c81016020851015611e475750805b601f840160051c820191505b81811015611e66575f8155600101611e53565b50505b505050565b81516001600160401b03811115611e8757611e87611829565b611e9b81611e958454611d1e565b84611e22565b6020601f821160018114611ecd575f8315611eb65750848201515b5f19600385901b1c1916600184901b178455611e66565b5f84815260208120601f198516915b82811015611efc5787850151825560209485019460019092019101611edc565b5084821015611f1957868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b87815260e060208201525f611f4060e08301896116c8565b8281036040840152611f5281896116c8565b90508281036060840152611f6681886116c8565b90508281036080840152611f7a81876116c8565b60a0840195909552505060c0015295945050505050565b848152836020820152608060408201525f611faf60808301856116c8565b8281036060840152611fc181856116c8565b979650505050505050565b606081525f611fde60608301866116c8565b8281036020840152611ff081866116c8565b9050828103604084015280845180835260208301915060208160051b840101602087015f5b8381101561204757601f198684030185526120318383516116c8565b6020958601959093509190910190600101612015565b5090999850505050505050505056fea26469706673582212207f8d95314cfa69a1737b7beeb517f884faa08830f157cfc6799770df533e321664736f6c634300081a0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATESHIPMENT = "createShipment";

    public static final String FUNC_GETSHIPMENT = "getShipment";

    public static final String FUNC_GETTRANSFERHISTORY = "getTransferHistory";

    public static final String FUNC_GETUSER = "getUser";

    public static final String FUNC_REGISTERUSER = "registerUser";

    public static final String FUNC_SHIPMENTTRANSFER = "shipmentTransfer";

    public static final Event SHIPMENTCREATED_EVENT = new Event("ShipmentCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event SHIPMENTTRANSFER_EVENT = new Event("ShipmentTransfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
    ;

    public static final Event USERREGISTERED_EVENT = new Event("UserRegistered", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<DynamicArray<Utf8String>>() {}));
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
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.origin = (String) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.destination = (String) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.units = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.weight = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
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
        typedResponse.description = (String) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.origin = (String) eventValues.getNonIndexedValues().get(3).getValue();
        typedResponse.destination = (String) eventValues.getNonIndexedValues().get(4).getValue();
        typedResponse.units = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
        typedResponse.weight = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
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
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.newState = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.transferNotes = (String) eventValues.getNonIndexedValues().get(3).getValue();
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
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.newState = (String) eventValues.getNonIndexedValues().get(2).getValue();
        typedResponse.transferNotes = (String) eventValues.getNonIndexedValues().get(3).getValue();
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
            typedResponse.email = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.roles = (List<String>) ((Array) eventValues.getNonIndexedValues().get(2)).getNativeValueCopy();
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
        typedResponse.email = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.roles = (List<String>) ((Array) eventValues.getNonIndexedValues().get(2)).getNativeValueCopy();
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

    public RemoteFunctionCall<Shipment> getShipment(BigInteger shipmentId) {
        final Function function = new Function(FUNC_GETSHIPMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(shipmentId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Shipment>() {}));
        return executeRemoteCallSingleValueReturn(function, Shipment.class);
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
            String newShipmentOwner, String newState, String transferNotes) {
        final Function function = new Function(
                FUNC_SHIPMENTTRANSFER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(shipmentId), 
                new org.web3j.abi.datatypes.Address(160, newShipmentOwner), 
                new org.web3j.abi.datatypes.Utf8String(newState), 
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

    public static class Shipment extends DynamicStruct {
        public BigInteger id;

        public String name;

        public String description;

        public String origin;

        public String destination;

        public BigInteger units;

        public BigInteger weight;

        public String currentState;

        public String currentOwner;

        public List<BigInteger> transferHistory;

        public Shipment(BigInteger id, String name, String description, String origin,
                String destination, BigInteger units, BigInteger weight, String currentState,
                String currentOwner, List<BigInteger> transferHistory) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id), 
                    new org.web3j.abi.datatypes.Utf8String(name), 
                    new org.web3j.abi.datatypes.Utf8String(description), 
                    new org.web3j.abi.datatypes.Utf8String(origin), 
                    new org.web3j.abi.datatypes.Utf8String(destination), 
                    new org.web3j.abi.datatypes.generated.Uint256(units), 
                    new org.web3j.abi.datatypes.generated.Uint256(weight), 
                    new org.web3j.abi.datatypes.Utf8String(currentState), 
                    new org.web3j.abi.datatypes.Address(160, currentOwner), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                            org.web3j.abi.datatypes.generated.Uint256.class,
                            org.web3j.abi.Utils.typeMap(transferHistory, org.web3j.abi.datatypes.generated.Uint256.class)));
            this.id = id;
            this.name = name;
            this.description = description;
            this.origin = origin;
            this.destination = destination;
            this.units = units;
            this.weight = weight;
            this.currentState = currentState;
            this.currentOwner = currentOwner;
            this.transferHistory = transferHistory;
        }

        public Shipment(Uint256 id, Utf8String name, Utf8String description, Utf8String origin,
                Utf8String destination, Uint256 units, Uint256 weight, Utf8String currentState,
                Address currentOwner,
                @Parameterized(type = Uint256.class) DynamicArray<Uint256> transferHistory) {
            super(id, name, description, origin, destination, units, weight, currentState, currentOwner, transferHistory);
            this.id = id.getValue();
            this.name = name.getValue();
            this.description = description.getValue();
            this.origin = origin.getValue();
            this.destination = destination.getValue();
            this.units = units.getValue();
            this.weight = weight.getValue();
            this.currentState = currentState.getValue();
            this.currentOwner = currentOwner.getValue();
            this.transferHistory = transferHistory.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
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

        public String username;

        public List<String> roles;

        public String email;

        public User(String userAddress, String username, List<String> roles, String email) {
            super(new org.web3j.abi.datatypes.Address(160, userAddress), 
                    new org.web3j.abi.datatypes.Utf8String(username), 
                    new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                            org.web3j.abi.datatypes.Utf8String.class,
                            org.web3j.abi.Utils.typeMap(roles, org.web3j.abi.datatypes.Utf8String.class)), 
                    new org.web3j.abi.datatypes.Utf8String(email));
            this.userAddress = userAddress;
            this.username = username;
            this.roles = roles;
            this.email = email;
        }

        public User(Address userAddress, Utf8String username,
                @Parameterized(type = Utf8String.class) DynamicArray<Utf8String> roles,
                Utf8String email) {
            super(userAddress, username, roles, email);
            this.userAddress = userAddress.getValue();
            this.username = username.getValue();
            this.roles = roles.getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
            this.email = email.getValue();
        }
    }

    public static class ShipmentCreatedEventResponse extends BaseEventResponse {
        public String owner;

        public BigInteger shipmentId;

        public String productName;

        public String description;

        public String origin;

        public String destination;

        public BigInteger units;

        public BigInteger weight;
    }

    public static class ShipmentTransferEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newShipmentOwner;

        public BigInteger shipmentId;

        public BigInteger timestamp;

        public String newState;

        public String transferNotes;
    }

    public static class UserRegisteredEventResponse extends BaseEventResponse {
        public String userAddress;

        public String name;

        public String email;

        public List<String> roles;
    }
}
