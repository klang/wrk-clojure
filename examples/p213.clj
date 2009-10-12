;; Making a Lancet DSL

;; Import previous work
(use 'lancet.step-2-complete 'lancet.step-3-complete)

;; Creating Lancet Targets
;; (runonce f) -> [has-run-fn reset-fn once-fn]

;; (deftarget fname docstring & forms)

;; (deftarget boo "doc" (println "boo!"))
;; should expand to
(let [[has-run-fn reset-fn once-fn] (runonce #(println "boo!"))]
  (def #^{:has-run-fn has-run-fn :reset-fn reset-fn :doc "doc"} 
       boo once-fn))
(boo)

(meta #'boo)

(defn has-run? [v]
  ((:has-run-fn (meta v))))
(has-run? #'boo)
(defmacro has-run? [f]
  `((:has-run-fn (meta (var ~f)))))
(has-run? boo)

(defmacro reset [f]
  `((:reset-fn (meta (var ~f)))))
(reset boo)

;; expansion should generate
(let [[has-run-fn reset-fn once-fn] (runonce #(println "boo!"))]
  (def #^{:has-run-fn has-run-fn 
	  :reset-fn reset-fn 
	  :doc "doc"}
       boo 
       once-fn))

;; wrap wanted expansion in defmacro with wanted variables
(defmacro deftarget [sym doc & forms]
  (let [[has-run-fn reset-fn once-fn] (runonce #(println "boo!"))]
    (def #^{:has-run-fn has-run-fn 
	    :reset-fn reset-fn 
	    :doc "doc"}
	 boo 
	 once-fn)))

;; syntax-quote the expansion and plug in the arguments
(defmacro deftarget [sym doc & forms]
  `(let [[has-run-fn reset-fn once-fn] (runonce (fn [] ~@forms))]
    (def #^{:has-run-fn has-run-fn 
	    :reset-fn reset-fn 
	    :doc ~doc}
	 ~sym 
	 once-fn)))

;; #^ is also a reader macro (like #) and can not be used in macros
(defmacro deftarget [sym doc & forms]
  `(let [[has-run-fn reset-fn once-fn] (runonce (fn [] ~@forms))]
     (def ~(with-meta 
	     sym 
	     {:has-run-fn has-run-fn 
	      :reset-fn reset-fn 
	      :doc doc}
	     once-fn))))

;; solve symbol naming with gensym
(defmacro deftarget [sym doc & forms]
  (let [has-run-fn (gensym "hr-") reset-fn (gensym "rf-")]
  `(let [[~has-run-fn ~reset-fn once-fn#] (runonce (fn [] ~@forms))]
     (def ~(with-meta 
	     sym 
	     {:has-run-fn has-run-fn :reset-fn reset-fn :doc doc})
	     once-fn#))))

(binding [*print-meta* true]
  (prn (macroexpand-1 
	'(deftarget foo "docstring" (println "hello")))))

(deftarget foo "demo function"
  (println "There can be only one!"))

;; Defining Ant Tasks for Lancet

(def echo (instantiate-task ant-project "echo" {:message "hello"}))
(.execute echo)

(echo {:message "hello"})

;; (define-ant-task ant-echo echo)
;;   should expand to
(defn ant-echo [props]
  (let [task (instantiate-task ant-project "echo" props)]
    (.execute task)
    task))

(ant-echo {:message "hello"})

(defmacro define-ant-task [clj-name ant-name]
  `(defn ~clj-name [props#]
     (let [task# (instantiate-task ant-project ~(name ant-name) props#)]
       (.execute task#)
       task#)))

(define-ant-task echo echo)
(echo {:message "hello"})

(define-ant-task mkdir mkdir)
(mkdir {:dir (java.io.File. "foo")})

(defn task-names []
  (map symbol (sort (.. ant-project getTaskDefinitions keySet))))
(task-names)

(defmacro define-all-ant-tasks []
  `(do ~@(map (fn [n] `(define-ant-task ~n ~n)) (task-names))))

(macroexpand-1 '(define-all-ant-tasks))

(define-all-ant-tasks)

(defn safe-ant-name [n]
  (if (resolve n) (symbol (str "ant-" n)) n))

(defmacro define-all-ant-tasks []
  `(do ~@(map (fn [n] `(define-ant-task ~(safe-ant-name n) ~n)) (task-names))))

(define-all-ant-tasks)
(sleep {:seconds 5})


