(use '[clojure.inspector :only (inspect inspect-tree)])

(inspect-tree (System/getProperties))

(inspect-tree {:clojure {:creator "Rich" :runs-on {:jvm true :clr true}} })