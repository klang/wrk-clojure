;;test-is
(use :reload '[clojure.contrib.test-is :only (is)])

(is (string? 10))

(is (instance? java.util.Collection "foo"))

(is (= "power" "wisdom"))

(defn #^Class class
  "Returns the Class of x"
  [#^Object x] (if (nil? x) x (. x (getClass))))

(defmulti  my-class identity)
(defmethod my-class nil [_] nil)
(defmethod my-class :default [x] (.getClass x))
