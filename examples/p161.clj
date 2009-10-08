;; alter

(defstruct message :sender :text)

(struct message "stu" "test message")

(def messages (ref ()))

;; bad idea
(defn naive-add-message [msg]
  (dosync (ref-set messages (cons msg @messages))))

;; (alter ref update-fn & args)

(defn add-message [msg] 
  (dosync (alter messages conj msg)))

;; (cons item sequence)
;; (conj sequence item) <-- more suitable for use with alter

;; (your-func thing-that-gets-updated & optional-other-args)

(add-message (struct message "user 1" "hello"))

@messages
(deref messages)
;; Adding Validation to Refs == key constraints on a table in a database

;; (ref initial-state options*)
;; options include:
;;  :validator validate-fn
;;  :meta metadata-map
(def validate-message-list
     (partial every? #(and (:sender %) (:text %))))

(def messages (ref () :validator validate-message-list))

(add-message "not a valid message")
;;Invalid reference state
;;  [Thrown class java.lang.IllegalStateException]

(add-message (struct message "stu" "legit message"))

