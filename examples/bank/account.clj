;; (use 'clojure.contrib.repl-utils)
(ns examples.bank.account)
(alias 'acc 'examples.bank.account)
(defstruct account :id :tag :balance)

(comment
(struct account 1 ::Savings 100M)
(def test-savings (struct account 1 ::Savings 100M))
(def test-checking (struct account 2 ::Checking 250M))
(interest-rate test-savings)
)

(defmulti interest-rate :tag)
(defmethod interest-rate ::Checking [_] 0M)
(defmethod interest-rate ::Savings [_] 0.05M)

(comment
(account-level (struct account 1 ::Savings 2000M))
(account-level (struct account 1 ::Checking 2000M))
)

(defmulti account-level :tag)
;;(defmethod account-level ::acc/Checking [acct])
(defmethod account-level ::Checking [acct]
  (if (>= (:balance acct) 5000) ::Premium ::Basic))
(defmethod account-level ::Savings [acct]
  (if (>= (:balance acct) 1000) ::Premium ::Basic))

(defmulti service-charge (fn [acct] [(account-level acct) (:tag acct)]))
(defmethod service-charge [::Basic ::Checking] [_] 25)
(defmethod service-charge [::Basic ::Savings] [_] 10)
(defmethod service-charge [::Premium ::Checking] [_] 0)
(defmethod service-charge [::Premium ::Savings] [_] 0)

(comment
(service-charge {:tag ::Checking :balance 1000})
(service-charge {:tag ::Savings :balance 1000})
)

(derive ::Savings ::Account)
(derive ::Checking ::Account)
(comment
(isa? ::Savings ::Account))


(defmulti service-charge (fn [acct] [(account-level acct) (:tag acct)]))
(defmethod service-charge [::Basic ::Checking] [_] 25)
(defmethod service-charge [::Basic ::Savings] [_] 10)
(defmethod service-charge [::Premium ::Account] [_] 0)


;; to use the account from ns user (i.e. the REPL):
(comment
(alias 'acc 'examples.bank.account)
(struct acc/account 1 ::acc/Savings 100M)
;(def test-savings (struct acc/account 1 ::acc/Savings 100M))
;(def test-checking (struct acc/account 2 ::acc/Checking 250M))
;(acc/interest-rate test-savings)
;(acc/account-level (struct acc/account 1 ::acc/Savings 2000M))
;(acc/account-level (struct acc/account 1 ::acc/Checking 2000M))
;(isa? ::acc/Savings ::acc/Account)
)
