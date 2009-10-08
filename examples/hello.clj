(ns examples.hello
    (:gen-class))
 
(defn -main
  [greetee]
  (new examples.hello)
  (println (str "Hello " greetee "!")))

;; compile like this:
(compile 'examples.hello)

;; and run like this:
;; >> java -cp ./classes:clojure.jar clojure.examples.hello Fred
;; Hello Fred!