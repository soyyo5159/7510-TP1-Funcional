(ns error-test
    (:require 
        [clojure.test :refer :all]
        [Error :refer :all]
    )
)
(defn suma [& args] (reduce + 0 args))
(defn orr [& args] (reduce (fn [x y] (or x y)) false args))

(deftest errores
    (testing "son error o no"
        (is (error? nil))
        (is (error? (error "mal")))
        (is (not (error? "mal")))
        (is (not (error? 7)))
        (is (not (error?  (list 4 5 6)   )))
    )
    (testing "fmap sin errores devuelve el resultado de la funcion"
        (is (= (fmap suma 2 2 2) 6))
        (is (= (fmap suma 4 3 2) 9))
        (is (= (fmap orr false false false) false))
        (is (= (fmap orr false true false) true))
    )

    (testing "fmap con errores devuelve error siempre"
        (is (not (= (fmap suma 2 2 (error "desconocido")) 6)) )
        (is (error? (fmap suma 2 2 (error "desconocido")) )   )
        (is (not (= (fmap orr true (error "desconocido")) true)) )
        (is (error? (fmap orr false (error "desconocido")) )   )
    )
)
