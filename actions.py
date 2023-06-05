# This files contains your custom actions which can be used to run
# custom Python code.
#
# See this guide on how to implement these action:
# https://rasa.com/docs/rasa/custom-actions


# This is a simple example for a custom action which utters "Hello World!"

# from typing import Any, Text, Dict, List
#
# from rasa_sdk import Action, Tracker
# from rasa_sdk.executor import CollectingDispatcher
#
#
# class ActionHelloWorld(Action):
#
#     def name(self) -> Text:
#         return "action_hello_world"
#
#     def run(self, dispatcher: CollectingDispatcher,
#             tracker: Tracker,
#             domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
#
#         dispatcher.utter_message(text="Hello World!")
#
#         return []

from typing import Any, Text, Dict, List

from rasa_sdk import Action, Tracker
from rasa_sdk.executor import CollectingDispatcher

class ActionCardRelated(Action):
    def name(self) -> Text:
        return "action_card_related"
        
    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        
        entities = tracker.latest_message['entities']
        print(entities)

        for e in entities:
            if e['entity'] == 'issue':
                observe = e['value']
                print(observe)
                if(observe == 'lost' or observe =='stolen' or observe == 'blocked'):
                    dispatcher.utter_message(text="Extemely sad to know!!!\nPlease don't panic.\n")
                    dispatcher.utter_message(text="Kindly,contact bank asap.\n---Contact Number: +977-01-4712221---")
                elif(observe == 'expired' or observe == 'damaged' or observe == 'broke' or observe == 'renew' or observe == 'get new' or observe == 'renew'):
                    dispatcher.utter_message(text="Please bear some patience\nYou have to apply for new card\n")
                    dispatcher.utter_message(text="To get new ATM card, please visit bank with ID proof")
                    dispatcher.utter_message(text="OR")
                    dispatcher.utter_message(text="Give us call at (01-4123211)")
                    dispatcher.utter_message(text="OR")
                    dispatcher.utter_message(text="Please e-mail us at atmcards@abcbank.com")
                elif(observe == 'forgotten' or observe == 'forgot' or observe == 'change'):
                            dispatcher.utter_message(text="Don't worry\nWe will help to recover your ATM PIN")
                            dispatcher.utter_message(text="SMS 'RESET PIN' to 5252\nOR\nLogin to NetBanking->ATM Settings->Change ATM Password PIN")
                elif(observe == 'not working'):
                    dispatcher.utter_message(text="Your card may not work because of a number of reasons\n1.Might be blocked\n2.EMV chip could be damaged\n3.May be expired")
                    dispatcher.utter_message(text="You can handover card to bank for investigation")
            if e['entity'] == 'range':
                dispatcher.utter_message(text="You can withdraw Rs. 40,000 per day using your card")
            if e['entity'] == 'cardvariety':
                 dispatcher.utter_message(text="You can select cards based on your need\n1.Normal Debit Card\n2.Normal Credit Card\n3.Global Debit Card\n4.Rupay Debit Card")
        return []

class ActionSecurityRelated(Action):

    def name(self) -> Text:
        return "action_security_related"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        entities = tracker.latest_message['entities']
        print(entities)

        for e in entities:
             if e['entity'] == 'security':
                  if e['value'] == 'verify':
                       dispatcher.utter_message(text="You will get OTP in registered number/email")
                  if e['value'] == 'receive' or e['value'] == 'collect':
                       dispatcher.utter_message(text="Bank handovers sensitive documents only to account holder")
                  if e['value'] == 'share':
                       dispatcher.utter_message(text="You MUST NOT share OTP/PIN or any other sensitive information to others")
        return []
        
class ActionOpeningAccount(Action):

    def name(self) -> Text:
        return "action_account_create"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        entities = tracker.latest_message['entities']
        acctype = tracker.get_slot("types")
        print(entities)
        print(acctype)
        for e in entities:
            if e['entity'] == 'mode' and e['value'] == 'online':
                if acctype:
                    dispatcher.utter_message(text=f"Please visit at www.abcbank.com/accounts/{acctype}/new to create {acctype} acount online")
                else:
                    dispatcher.utter_message(text="Please visit at www.abcbank.com/accounts/new to create acount online") 
                return[]
        if acctype:
            dispatcher.utter_message(text=f"Please visit bank with above mentioned documents for creating {acctype} account")
        else:
            dispatcher.utter_message(text="Please visit bank with above mentioned documents for creating account") 
        return []
    
class ActionAccountSupport(Action):

    def name(self) -> Text:
        return "action_account_support"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        
        entities = tracker.latest_message['entities']
        print(entities)
        for e in entities:
            if e['entity'] == 'service':
                service = e['value']
                if (service == 'interest' or service == 'rate' ):
                    dispatcher.utter_message(text="Please refer Bank's official website to know latest interest rates")
                if (service == 'locker'):
                    dispatcher.utter_message(text="Please contant if bank offers any locker facilities")
                if (service == "mobile banking" or service == "netbanking" or service == 'sms banking' or service == 'internet banking'):
                    dispatcher.utter_message(text=f"Yes Bank mostly provides {service}\nThe terms and conditions vary bank to bank")

        return []
    
class ActionLoanSupport(Action):

    def name(self) -> Text:
        return "action_loan_support"

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:
        entities = tracker.latest_message['entities']

        print(entities)
        for e in entities:
            value = e['value'].lower()
            if  e['entity'] == 'measure' and (value == 'criteria' or value == 'eligible' or value == 'documents'):
                dispatcher.utter_message(text="You need A,B,C")
                return []
        for e in entities:
            if e['entity'] == 'loan':
                loan = e['value'].lower()

                if(loan == 'loan'):
                    dispatcher.utter_message(text="Your personal loan can vary based on your income slab\n")
        
                if(loan == 'bike' or loan== 'two wheeler'):
                    dispatcher.utter_message(text="This loan based on your credit score\nYou must have a credit score of 5 lakhs")
            
                if(loan == 'car' or  loan =="four wheeler"):
                    dispatcher.utter_message(text="Your  is mostly based on your credit score\nYou must have a credit score of 10-15 lakhs")
             
                if(loan == 'business'):
                    dispatcher.utter_message(text="For you need PAN number registerd\n")
            
                if(loan == 'education'):
                    dispatcher.utter_message(text="For  you need to submit recommendation letter from Ministry of Education\n")
            

        return []