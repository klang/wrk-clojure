;; an easy placeholder to a class
(def mkdir-task (org.apache.tools.ant.taskdefs.Mkdir.))
;; check which methods the class has
(map #(.getName %) (.getMethods (class mkdir-task)))

;; just try execute .. 
(.execute mkdir-task)
;; --> a dir is missing

;; trying to set a dir
(.setDir mkdir-task "sample-dir")
;; not as a string

(filter #(= "setDir" (.getName %)) 
	(.getMethods (class mkdir-task)))
;; (#<Method public void org.apache.tools.ant.taskdefs.Mkdir.setDir(java.io.File)>)

;; it expects a file
(.setDir mkdir-task (java.io.File. "sample-dir"))
(.execute mkdir-task)
;; dir will be created in ~/

;(def project (org.apache.tools.ant.Project.))
;(def logger (org.apache.tools.ant.NoBannerLogger.))
;(.setOutputPrintStream logger System/out)
;(.setErrorPrintStream logger System/err)
;(.setMessageOutputLevel logger org.apache.tools.ant.Project/MSG_INFO)
;(.init project)
;(.addBuildListener project logger)

;; -- or the easy way to do the same thing:

(def
 #^{:doc "Dummy ant project to keep Ant tasks happy"}
 ant-project
 (let [proj (org.apache.tools.ant.Project.)
       logger (org.apache.tools.ant.NoBannerLogger.)]
   (doto logger
     (.setMessageOutputLevel org.apache.tools.ant.Project/MSG_INFO)
     (.setOutputPrintStream System/out)
     (.setErrorPrintStream System/err))
   (doto proj
     (.init)
     (.addBuildListener logger))))
;; after this, ant-project will be bound to the 'let'
ant-project
;; to see the project reference
(bean ant-project)
;; .. to look inside the object

(keys (bean ant-project))
;; .. to get the property names (easier to look at)

(:buildListeners (bean ant-project))


(def echo-task (.createTask ant-project "echo"))

(.setMessage echo-task "hello ant")
(.execute echo-task)

(defn instantiate-task [project name]
  (let [task (.createTask project name)]
    (doto task
      (.init)
      (.setProject project))))

(def echo-task (instantiate-task ant-project "echo"))
(.setMessage echo-task "echo from instantiate-task")
(.execute echo-task)

;;(def echo-task (instantiate-task ant-project "sisyphus"))

(use '[clojure.contrib.except :only (throw-if)])

;; catch the strange null pointer exception
;; and throw a more meaningfull one
(defn safe-instantiate-task [project name]
  (let [task (.createTask project name)]
    (throw-if (nil? task)
	      IllegalArgumentException (str "no task named " name))
    (doto task
      (.init)
      (.setProject project))))

(def echo-task (safe-instantiate-task ant-project "sisyphus"))