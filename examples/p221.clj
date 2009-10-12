(use 'lancet.step-1-complete)

(def echo-task (instantiate-task ant-project "echo"))

(.setMessage echo-task "some message")

;; TODO: enable this signature
;; (instantiate-task ant-project "echo" {:message "hello"})

(import '(java.beans Introspector))
(Introspector/getBeanInfo (class echo-task))
(.getPropertyDescriptors *1)
;;(.getPropertyDescriptors (Introspector/getBeanInfo (class echo-task)))
(def prop-descs *1)
(def prop-descs (.getPropertyDescriptors (Introspector/getBeanInfo (class echo-task))))
(count prop-descs)
(bean (first prop-descs))

(defn property-descriptor [inst prop-name]
  (first
   (filter #(= (name prop-name) (.getName %))
	   (.getPropertyDescriptors
	    (Introspector/getBeanInfo (class inst))))))

(bean (property-descriptor echo-task :message))

(use '[clojure.contrib.except :only (throw-if)])
(defn set-property! [inst prop value]
  (let [pd (property-descriptor inst prop)]
    (throw-if (nil? pd) (str "No suc property " prop))
    (.invoke (.getWriteMethod pd) inst (into-array [value])))) ;; invoke needs a java array

(set-property! echo-task :message "a new message!")

(.execute echo-task)

;; (doseq bindings & body)
(defn set-properties! [inst prop-map]
  (doseq [[k v] prop-map] (set-property! inst k v)))

(set-properties! echo-task {:message "yet another message"})

(.execute echo-task)

;; we have to call the function something else because we "used" the first step of the previously
;; apparently it is illegal to overwrite stuff you "use" .. 
(defn instantiate-task-step-2 [project name props]
  (let [task (.createTask project name)]
    (throw-if (nil? task) 
	      IllegalArgumentException (str "No task named " name))  
    (doto task
      (.init)
      (.setProject project)
      (set-properties! props))
    task))

(def echo-with-msg
     (instantiate-task-step-2 ant-project "echo" {:message "hello"}))


(.execute echo-with-msg)