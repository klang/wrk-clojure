(def validate-message-list
     (partial every? #(and (:sender %) (:text %))))
(def messages (ref () :validator validate-message-list))

;; Including Agents in Transactions

(def backup-agent (agent "output/messages-backup.clj"))

(use ' [clojure.contrib.duck-streams :only (spit)])

(defn add-message-with-backup [msg]
  (dosync
   (let [snapshot (commute messages conj msg)]
     (send-off backup-agent (fn [filename]
			      (spit filename snapshot)
			      filename))
     snapshot)))

(def foo 10)
foo
;; globally bound foo is visible from all Threads
(.start (Thread. (fn [] (println foo))))

(binding [foo 42] foo)

(defn print-foo [] (println foo))

(let [foo "let foo"] (print-foo))

;; shadow the globally bound foo with a binding
(binding [foo "bound foo"] (print-foo))


;; Acting at a Distance

(defn slow-double [n]
  (Thread/sleep 100)
  (* n 2))

(defn calls-slow-double []
  (map slow-double [1 2 1 2 1 2]))

(time (dorun (calls-slow-double)))

;(memoize function)

(defn demo-memoize []
  (time
   (dorun
    (binding [slow-double (memoize slow-double)]
      (calls-slow-double)))))

(demo-memoize)