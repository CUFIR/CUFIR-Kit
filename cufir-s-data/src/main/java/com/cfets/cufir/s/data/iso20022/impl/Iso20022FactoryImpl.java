/**
 */
package com.cfets.cufir.s.data.iso20022.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import com.cfets.cufir.s.data.iso20022.Address;
import com.cfets.cufir.s.data.iso20022.Aggregation;
import com.cfets.cufir.s.data.iso20022.Amount;
import com.cfets.cufir.s.data.iso20022.Binary;
import com.cfets.cufir.s.data.iso20022.BroadcastList;
import com.cfets.cufir.s.data.iso20022.BusinessArea;
import com.cfets.cufir.s.data.iso20022.BusinessAssociationEnd;
import com.cfets.cufir.s.data.iso20022.BusinessAttribute;
import com.cfets.cufir.s.data.iso20022.BusinessComponent;
import com.cfets.cufir.s.data.iso20022.BusinessProcess;
import com.cfets.cufir.s.data.iso20022.BusinessProcessCatalogue;
import com.cfets.cufir.s.data.iso20022.BusinessRole;
import com.cfets.cufir.s.data.iso20022.BusinessTransaction;
import com.cfets.cufir.s.data.iso20022.ChoiceComponent;
import com.cfets.cufir.s.data.iso20022.Code;
import com.cfets.cufir.s.data.iso20022.CodeSet;
import com.cfets.cufir.s.data.iso20022.Constraint;
import com.cfets.cufir.s.data.iso20022.ConvergenceDocumentation;
import com.cfets.cufir.s.data.iso20022.Conversation;
import com.cfets.cufir.s.data.iso20022.DataDictionary;
import com.cfets.cufir.s.data.iso20022.Date;
import com.cfets.cufir.s.data.iso20022.DateTime;
import com.cfets.cufir.s.data.iso20022.Day;
import com.cfets.cufir.s.data.iso20022.Decimal;
import com.cfets.cufir.s.data.iso20022.DeliveryAssurance;
import com.cfets.cufir.s.data.iso20022.Doclet;
import com.cfets.cufir.s.data.iso20022.Durability;
import com.cfets.cufir.s.data.iso20022.Duration;
import com.cfets.cufir.s.data.iso20022.Encoding;
import com.cfets.cufir.s.data.iso20022.EndPointCategory;
import com.cfets.cufir.s.data.iso20022.ExternalSchema;
import com.cfets.cufir.s.data.iso20022.ISO15022MessageSet;
import com.cfets.cufir.s.data.iso20022.ISO20022Version;
import com.cfets.cufir.s.data.iso20022.IdentifierSet;
import com.cfets.cufir.s.data.iso20022.Indicator;
import com.cfets.cufir.s.data.iso20022.IndustryMessageSet;
import com.cfets.cufir.s.data.iso20022.Iso20022Factory;
import com.cfets.cufir.s.data.iso20022.Iso20022Package;
import com.cfets.cufir.s.data.iso20022.MessageAssociationEnd;
import com.cfets.cufir.s.data.iso20022.MessageAttribute;
import com.cfets.cufir.s.data.iso20022.MessageBuildingBlock;
import com.cfets.cufir.s.data.iso20022.MessageCasting;
import com.cfets.cufir.s.data.iso20022.MessageChoreography;
import com.cfets.cufir.s.data.iso20022.MessageComponent;
import com.cfets.cufir.s.data.iso20022.MessageDefinition;
import com.cfets.cufir.s.data.iso20022.MessageDefinitionIdentifier;
import com.cfets.cufir.s.data.iso20022.MessageDeliveryOrder;
import com.cfets.cufir.s.data.iso20022.MessageInstance;
import com.cfets.cufir.s.data.iso20022.MessageSet;
import com.cfets.cufir.s.data.iso20022.MessageTransmission;
import com.cfets.cufir.s.data.iso20022.MessageTransportMode;
import com.cfets.cufir.s.data.iso20022.MessageTransportSystem;
import com.cfets.cufir.s.data.iso20022.MessageValidationLevel;
import com.cfets.cufir.s.data.iso20022.MessageValidationOnOff;
import com.cfets.cufir.s.data.iso20022.MessageValidationResults;
import com.cfets.cufir.s.data.iso20022.MessagingEndpoint;
import com.cfets.cufir.s.data.iso20022.Month;
import com.cfets.cufir.s.data.iso20022.MonthDay;
import com.cfets.cufir.s.data.iso20022.Namespace;
import com.cfets.cufir.s.data.iso20022.Participant;
import com.cfets.cufir.s.data.iso20022.ProcessContent;
import com.cfets.cufir.s.data.iso20022.Quantity;
import com.cfets.cufir.s.data.iso20022.Rate;
import com.cfets.cufir.s.data.iso20022.Receive;
import com.cfets.cufir.s.data.iso20022.ReceiverAsynchronicity;
import com.cfets.cufir.s.data.iso20022.RegistrationStatus;
import com.cfets.cufir.s.data.iso20022.Repository;
import com.cfets.cufir.s.data.iso20022.SchemaType;
import com.cfets.cufir.s.data.iso20022.SchemaTypeKind;
import com.cfets.cufir.s.data.iso20022.SemanticMarkup;
import com.cfets.cufir.s.data.iso20022.SemanticMarkupElement;
import com.cfets.cufir.s.data.iso20022.Send;
import com.cfets.cufir.s.data.iso20022.SenderAsynchronicity;
import com.cfets.cufir.s.data.iso20022.Syntax;
import com.cfets.cufir.s.data.iso20022.SyntaxMessageScheme;
import com.cfets.cufir.s.data.iso20022.Text;
import com.cfets.cufir.s.data.iso20022.Time;
import com.cfets.cufir.s.data.iso20022.TransportMessage;
import com.cfets.cufir.s.data.iso20022.UserDefined;
import com.cfets.cufir.s.data.iso20022.Xor;
import com.cfets.cufir.s.data.iso20022.Year;
import com.cfets.cufir.s.data.iso20022.YearMonth;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Iso20022FactoryImpl extends EFactoryImpl implements Iso20022Factory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Iso20022Factory init() {
		try {
			Iso20022Factory theIso20022Factory = (Iso20022Factory)EPackage.Registry.INSTANCE.getEFactory(Iso20022Package.eNS_URI);
			if (theIso20022Factory != null) {
				return theIso20022Factory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Iso20022FactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Iso20022FactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case Iso20022Package.ADDRESS: return createAddress();
			case Iso20022Package.BROADCAST_LIST: return createBroadcastList();
			case Iso20022Package.MESSAGING_ENDPOINT: return createMessagingEndpoint();
			case Iso20022Package.MESSAGE_TRANSPORT_SYSTEM: return createMessageTransportSystem();
			case Iso20022Package.TRANSPORT_MESSAGE: return createTransportMessage();
			case Iso20022Package.MESSAGE_INSTANCE: return createMessageInstance();
			case Iso20022Package.SYNTAX_MESSAGE_SCHEME: return createSyntaxMessageScheme();
			case Iso20022Package.SEMANTIC_MARKUP: return createSemanticMarkup();
			case Iso20022Package.SEMANTIC_MARKUP_ELEMENT: return createSemanticMarkupElement();
			case Iso20022Package.DOCLET: return createDoclet();
			case Iso20022Package.CONSTRAINT: return createConstraint();
			case Iso20022Package.BUSINESS_PROCESS_CATALOGUE: return createBusinessProcessCatalogue();
			case Iso20022Package.REPOSITORY: return createRepository();
			case Iso20022Package.DATA_DICTIONARY: return createDataDictionary();
			case Iso20022Package.MESSAGE_DEFINITION: return createMessageDefinition();
			case Iso20022Package.MESSAGE_SET: return createMessageSet();
			case Iso20022Package.SYNTAX: return createSyntax();
			case Iso20022Package.ENCODING: return createEncoding();
			case Iso20022Package.BUSINESS_AREA: return createBusinessArea();
			case Iso20022Package.XOR: return createXor();
			case Iso20022Package.BUSINESS_COMPONENT: return createBusinessComponent();
			case Iso20022Package.MESSAGE_BUILDING_BLOCK: return createMessageBuildingBlock();
			case Iso20022Package.BUSINESS_ASSOCIATION_END: return createBusinessAssociationEnd();
			case Iso20022Package.MESSAGE_COMPONENT: return createMessageComponent();
			case Iso20022Package.MESSAGE_CHOREOGRAPHY: return createMessageChoreography();
			case Iso20022Package.BUSINESS_TRANSACTION: return createBusinessTransaction();
			case Iso20022Package.BUSINESS_PROCESS: return createBusinessProcess();
			case Iso20022Package.BUSINESS_ROLE: return createBusinessRole();
			case Iso20022Package.PARTICIPANT: return createParticipant();
			case Iso20022Package.RECEIVE: return createReceive();
			case Iso20022Package.MESSAGE_TRANSMISSION: return createMessageTransmission();
			case Iso20022Package.SEND: return createSend();
			case Iso20022Package.MESSAGE_TRANSPORT_MODE: return createMessageTransportMode();
			case Iso20022Package.MESSAGE_DEFINITION_IDENTIFIER: return createMessageDefinitionIdentifier();
			case Iso20022Package.CONVERSATION: return createConversation();
			case Iso20022Package.MESSAGE_ASSOCIATION_END: return createMessageAssociationEnd();
			case Iso20022Package.MESSAGE_ATTRIBUTE: return createMessageAttribute();
			case Iso20022Package.BUSINESS_ATTRIBUTE: return createBusinessAttribute();
			case Iso20022Package.TEXT: return createText();
			case Iso20022Package.STRING: return createString();
			case Iso20022Package.IDENTIFIER_SET: return createIdentifierSet();
			case Iso20022Package.INDICATOR: return createIndicator();
			case Iso20022Package.BOOLEAN: return createBoolean();
			case Iso20022Package.RATE: return createRate();
			case Iso20022Package.DECIMAL: return createDecimal();
			case Iso20022Package.EXTERNAL_SCHEMA: return createExternalSchema();
			case Iso20022Package.QUANTITY: return createQuantity();
			case Iso20022Package.CODE: return createCode();
			case Iso20022Package.CODE_SET: return createCodeSet();
			case Iso20022Package.AMOUNT: return createAmount();
			case Iso20022Package.CHOICE_COMPONENT: return createChoiceComponent();
			case Iso20022Package.END_POINT_CATEGORY: return createEndPointCategory();
			case Iso20022Package.BINARY: return createBinary();
			case Iso20022Package.DATE: return createDate();
			case Iso20022Package.DATE_TIME: return createDateTime();
			case Iso20022Package.DAY: return createDay();
			case Iso20022Package.DURATION: return createDuration();
			case Iso20022Package.MONTH: return createMonth();
			case Iso20022Package.MONTH_DAY: return createMonthDay();
			case Iso20022Package.TIME: return createTime();
			case Iso20022Package.YEAR: return createYear();
			case Iso20022Package.YEAR_MONTH: return createYearMonth();
			case Iso20022Package.USER_DEFINED: return createUserDefined();
			case Iso20022Package.INDUSTRY_MESSAGE_SET: return createIndustryMessageSet();
			case Iso20022Package.CONVERGENCE_DOCUMENTATION: return createConvergenceDocumentation();
			case Iso20022Package.ISO15022_MESSAGE_SET: return createISO15022MessageSet();
			case Iso20022Package.SCHEMA_TYPE: return createSchemaType();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case Iso20022Package.REGISTRATION_STATUS:
				return createRegistrationStatusFromString(eDataType, initialValue);
			case Iso20022Package.AGGREGATION:
				return createAggregationFromString(eDataType, initialValue);
			case Iso20022Package.DELIVERY_ASSURANCE:
				return createDeliveryAssuranceFromString(eDataType, initialValue);
			case Iso20022Package.DURABILITY:
				return createDurabilityFromString(eDataType, initialValue);
			case Iso20022Package.MESSAGE_CASTING:
				return createMessageCastingFromString(eDataType, initialValue);
			case Iso20022Package.MESSAGE_DELIVERY_ORDER:
				return createMessageDeliveryOrderFromString(eDataType, initialValue);
			case Iso20022Package.MESSAGE_VALIDATION_LEVEL:
				return createMessageValidationLevelFromString(eDataType, initialValue);
			case Iso20022Package.MESSAGE_VALIDATION_ON_OFF:
				return createMessageValidationOnOffFromString(eDataType, initialValue);
			case Iso20022Package.MESSAGE_VALIDATION_RESULTS:
				return createMessageValidationResultsFromString(eDataType, initialValue);
			case Iso20022Package.RECEIVER_ASYNCHRONICITY:
				return createReceiverAsynchronicityFromString(eDataType, initialValue);
			case Iso20022Package.SENDER_ASYNCHRONICITY:
				return createSenderAsynchronicityFromString(eDataType, initialValue);
			case Iso20022Package.PROCESS_CONTENT:
				return createProcessContentFromString(eDataType, initialValue);
			case Iso20022Package.NAMESPACE:
				return createNamespaceFromString(eDataType, initialValue);
			case Iso20022Package.SCHEMA_TYPE_KIND:
				return createSchemaTypeKindFromString(eDataType, initialValue);
			case Iso20022Package.ISO20022_VERSION:
				return createISO20022VersionFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case Iso20022Package.REGISTRATION_STATUS:
				return convertRegistrationStatusToString(eDataType, instanceValue);
			case Iso20022Package.AGGREGATION:
				return convertAggregationToString(eDataType, instanceValue);
			case Iso20022Package.DELIVERY_ASSURANCE:
				return convertDeliveryAssuranceToString(eDataType, instanceValue);
			case Iso20022Package.DURABILITY:
				return convertDurabilityToString(eDataType, instanceValue);
			case Iso20022Package.MESSAGE_CASTING:
				return convertMessageCastingToString(eDataType, instanceValue);
			case Iso20022Package.MESSAGE_DELIVERY_ORDER:
				return convertMessageDeliveryOrderToString(eDataType, instanceValue);
			case Iso20022Package.MESSAGE_VALIDATION_LEVEL:
				return convertMessageValidationLevelToString(eDataType, instanceValue);
			case Iso20022Package.MESSAGE_VALIDATION_ON_OFF:
				return convertMessageValidationOnOffToString(eDataType, instanceValue);
			case Iso20022Package.MESSAGE_VALIDATION_RESULTS:
				return convertMessageValidationResultsToString(eDataType, instanceValue);
			case Iso20022Package.RECEIVER_ASYNCHRONICITY:
				return convertReceiverAsynchronicityToString(eDataType, instanceValue);
			case Iso20022Package.SENDER_ASYNCHRONICITY:
				return convertSenderAsynchronicityToString(eDataType, instanceValue);
			case Iso20022Package.PROCESS_CONTENT:
				return convertProcessContentToString(eDataType, instanceValue);
			case Iso20022Package.NAMESPACE:
				return convertNamespaceToString(eDataType, instanceValue);
			case Iso20022Package.SCHEMA_TYPE_KIND:
				return convertSchemaTypeKindToString(eDataType, instanceValue);
			case Iso20022Package.ISO20022_VERSION:
				return convertISO20022VersionToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Address createAddress() {
		AddressImpl address = new AddressImpl();
		return address;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BroadcastList createBroadcastList() {
		BroadcastListImpl broadcastList = new BroadcastListImpl();
		return broadcastList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessagingEndpoint createMessagingEndpoint() {
		MessagingEndpointImpl messagingEndpoint = new MessagingEndpointImpl();
		return messagingEndpoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageTransportSystem createMessageTransportSystem() {
		MessageTransportSystemImpl messageTransportSystem = new MessageTransportSystemImpl();
		return messageTransportSystem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransportMessage createTransportMessage() {
		TransportMessageImpl transportMessage = new TransportMessageImpl();
		return transportMessage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageInstance createMessageInstance() {
		MessageInstanceImpl messageInstance = new MessageInstanceImpl();
		return messageInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SyntaxMessageScheme createSyntaxMessageScheme() {
		SyntaxMessageSchemeImpl syntaxMessageScheme = new SyntaxMessageSchemeImpl();
		return syntaxMessageScheme;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SemanticMarkup createSemanticMarkup() {
		SemanticMarkupImpl semanticMarkup = new SemanticMarkupImpl();
		return semanticMarkup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SemanticMarkupElement createSemanticMarkupElement() {
		SemanticMarkupElementImpl semanticMarkupElement = new SemanticMarkupElementImpl();
		return semanticMarkupElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Doclet createDoclet() {
		DocletImpl doclet = new DocletImpl();
		return doclet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Constraint createConstraint() {
		ConstraintImpl constraint = new ConstraintImpl();
		return constraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessProcessCatalogue createBusinessProcessCatalogue() {
		BusinessProcessCatalogueImpl businessProcessCatalogue = new BusinessProcessCatalogueImpl();
		return businessProcessCatalogue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Repository createRepository() {
		RepositoryImpl repository = new RepositoryImpl();
		return repository;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DataDictionary createDataDictionary() {
		DataDictionaryImpl dataDictionary = new DataDictionaryImpl();
		return dataDictionary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageDefinition createMessageDefinition() {
		MessageDefinitionImpl messageDefinition = new MessageDefinitionImpl();
		return messageDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageSet createMessageSet() {
		MessageSetImpl messageSet = new MessageSetImpl();
		return messageSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Syntax createSyntax() {
		SyntaxImpl syntax = new SyntaxImpl();
		return syntax;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Encoding createEncoding() {
		EncodingImpl encoding = new EncodingImpl();
		return encoding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessArea createBusinessArea() {
		BusinessAreaImpl businessArea = new BusinessAreaImpl();
		return businessArea;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Xor createXor() {
		XorImpl xor = new XorImpl();
		return xor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessComponent createBusinessComponent() {
		BusinessComponentImpl businessComponent = new BusinessComponentImpl();
		return businessComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageBuildingBlock createMessageBuildingBlock() {
		MessageBuildingBlockImpl messageBuildingBlock = new MessageBuildingBlockImpl();
		return messageBuildingBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessAssociationEnd createBusinessAssociationEnd() {
		BusinessAssociationEndImpl businessAssociationEnd = new BusinessAssociationEndImpl();
		return businessAssociationEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageComponent createMessageComponent() {
		MessageComponentImpl messageComponent = new MessageComponentImpl();
		return messageComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageChoreography createMessageChoreography() {
		MessageChoreographyImpl messageChoreography = new MessageChoreographyImpl();
		return messageChoreography;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessTransaction createBusinessTransaction() {
		BusinessTransactionImpl businessTransaction = new BusinessTransactionImpl();
		return businessTransaction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessProcess createBusinessProcess() {
		BusinessProcessImpl businessProcess = new BusinessProcessImpl();
		return businessProcess;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessRole createBusinessRole() {
		BusinessRoleImpl businessRole = new BusinessRoleImpl();
		return businessRole;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Participant createParticipant() {
		ParticipantImpl participant = new ParticipantImpl();
		return participant;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Receive createReceive() {
		ReceiveImpl receive = new ReceiveImpl();
		return receive;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageTransmission createMessageTransmission() {
		MessageTransmissionImpl messageTransmission = new MessageTransmissionImpl();
		return messageTransmission;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Send createSend() {
		SendImpl send = new SendImpl();
		return send;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageTransportMode createMessageTransportMode() {
		MessageTransportModeImpl messageTransportMode = new MessageTransportModeImpl();
		return messageTransportMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageDefinitionIdentifier createMessageDefinitionIdentifier() {
		MessageDefinitionIdentifierImpl messageDefinitionIdentifier = new MessageDefinitionIdentifierImpl();
		return messageDefinitionIdentifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Conversation createConversation() {
		ConversationImpl conversation = new ConversationImpl();
		return conversation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageAssociationEnd createMessageAssociationEnd() {
		MessageAssociationEndImpl messageAssociationEnd = new MessageAssociationEndImpl();
		return messageAssociationEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MessageAttribute createMessageAttribute() {
		MessageAttributeImpl messageAttribute = new MessageAttributeImpl();
		return messageAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BusinessAttribute createBusinessAttribute() {
		BusinessAttributeImpl businessAttribute = new BusinessAttributeImpl();
		return businessAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Text createText() {
		TextImpl text = new TextImpl();
		return text;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public com.cfets.cufir.s.data.iso20022.String createString() {
		StringImpl string = new StringImpl();
		return string;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public IdentifierSet createIdentifierSet() {
		IdentifierSetImpl identifierSet = new IdentifierSetImpl();
		return identifierSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Indicator createIndicator() {
		IndicatorImpl indicator = new IndicatorImpl();
		return indicator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public com.cfets.cufir.s.data.iso20022.Boolean createBoolean() {
		BooleanImpl boolean_ = new BooleanImpl();
		return boolean_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Rate createRate() {
		RateImpl rate = new RateImpl();
		return rate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Decimal createDecimal() {
		DecimalImpl decimal = new DecimalImpl();
		return decimal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExternalSchema createExternalSchema() {
		ExternalSchemaImpl externalSchema = new ExternalSchemaImpl();
		return externalSchema;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Quantity createQuantity() {
		QuantityImpl quantity = new QuantityImpl();
		return quantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Code createCode() {
		CodeImpl code = new CodeImpl();
		return code;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CodeSet createCodeSet() {
		CodeSetImpl codeSet = new CodeSetImpl();
		return codeSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Amount createAmount() {
		AmountImpl amount = new AmountImpl();
		return amount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ChoiceComponent createChoiceComponent() {
		ChoiceComponentImpl choiceComponent = new ChoiceComponentImpl();
		return choiceComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EndPointCategory createEndPointCategory() {
		EndPointCategoryImpl endPointCategory = new EndPointCategoryImpl();
		return endPointCategory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Binary createBinary() {
		BinaryImpl binary = new BinaryImpl();
		return binary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Date createDate() {
		DateImpl date = new DateImpl();
		return date;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DateTime createDateTime() {
		DateTimeImpl dateTime = new DateTimeImpl();
		return dateTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Day createDay() {
		DayImpl day = new DayImpl();
		return day;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Duration createDuration() {
		DurationImpl duration = new DurationImpl();
		return duration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Month createMonth() {
		MonthImpl month = new MonthImpl();
		return month;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MonthDay createMonthDay() {
		MonthDayImpl monthDay = new MonthDayImpl();
		return monthDay;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Time createTime() {
		TimeImpl time = new TimeImpl();
		return time;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Year createYear() {
		YearImpl year = new YearImpl();
		return year;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public YearMonth createYearMonth() {
		YearMonthImpl yearMonth = new YearMonthImpl();
		return yearMonth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public UserDefined createUserDefined() {
		UserDefinedImpl userDefined = new UserDefinedImpl();
		return userDefined;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public IndustryMessageSet createIndustryMessageSet() {
		IndustryMessageSetImpl industryMessageSet = new IndustryMessageSetImpl();
		return industryMessageSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ConvergenceDocumentation createConvergenceDocumentation() {
		ConvergenceDocumentationImpl convergenceDocumentation = new ConvergenceDocumentationImpl();
		return convergenceDocumentation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ISO15022MessageSet createISO15022MessageSet() {
		ISO15022MessageSetImpl iso15022MessageSet = new ISO15022MessageSetImpl();
		return iso15022MessageSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SchemaType createSchemaType() {
		SchemaTypeImpl schemaType = new SchemaTypeImpl();
		return schemaType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegistrationStatus createRegistrationStatusFromString(EDataType eDataType, String initialValue) {
		RegistrationStatus result = RegistrationStatus.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRegistrationStatusToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Aggregation createAggregationFromString(EDataType eDataType, String initialValue) {
		Aggregation result = Aggregation.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAggregationToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeliveryAssurance createDeliveryAssuranceFromString(EDataType eDataType, String initialValue) {
		DeliveryAssurance result = DeliveryAssurance.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDeliveryAssuranceToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Durability createDurabilityFromString(EDataType eDataType, String initialValue) {
		Durability result = Durability.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDurabilityToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageCasting createMessageCastingFromString(EDataType eDataType, String initialValue) {
		MessageCasting result = MessageCasting.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMessageCastingToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageDeliveryOrder createMessageDeliveryOrderFromString(EDataType eDataType, String initialValue) {
		MessageDeliveryOrder result = MessageDeliveryOrder.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMessageDeliveryOrderToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageValidationLevel createMessageValidationLevelFromString(EDataType eDataType, String initialValue) {
		MessageValidationLevel result = MessageValidationLevel.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMessageValidationLevelToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageValidationOnOff createMessageValidationOnOffFromString(EDataType eDataType, String initialValue) {
		MessageValidationOnOff result = MessageValidationOnOff.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMessageValidationOnOffToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageValidationResults createMessageValidationResultsFromString(EDataType eDataType, String initialValue) {
		MessageValidationResults result = MessageValidationResults.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMessageValidationResultsToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReceiverAsynchronicity createReceiverAsynchronicityFromString(EDataType eDataType, String initialValue) {
		ReceiverAsynchronicity result = ReceiverAsynchronicity.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertReceiverAsynchronicityToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SenderAsynchronicity createSenderAsynchronicityFromString(EDataType eDataType, String initialValue) {
		SenderAsynchronicity result = SenderAsynchronicity.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSenderAsynchronicityToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcessContent createProcessContentFromString(EDataType eDataType, String initialValue) {
		ProcessContent result = ProcessContent.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertProcessContentToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Namespace createNamespaceFromString(EDataType eDataType, String initialValue) {
		Namespace result = Namespace.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertNamespaceToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SchemaTypeKind createSchemaTypeKindFromString(EDataType eDataType, String initialValue) {
		SchemaTypeKind result = SchemaTypeKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSchemaTypeKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ISO20022Version createISO20022VersionFromString(EDataType eDataType, String initialValue) {
		ISO20022Version result = ISO20022Version.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertISO20022VersionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Iso20022Package getIso20022Package() {
		return (Iso20022Package)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static Iso20022Package getPackage() {
		return Iso20022Package.eINSTANCE;
	}

} //Iso20022FactoryImpl
