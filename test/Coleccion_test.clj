(ns Coleccion-test(:require 
    [clojure.test :refer :all]
    [Coleccion :refer :all]
    [Cumplible :refer :all]
))

(defrecord Dato [dato] Cumplible
    (cumple? [yo fun] (fun dato))
)

(deftest depende-del-dato
    (testing "datos < 5 cumplen <5, conjuncion"
        (is (cumple?
            (conjuncion (Dato. 4) (Dato. 3) (Dato. 1))
            (partial > 5)
        ))
    )
    (testing "datos < 5 cumplen <5, disyuncion"
        (is (cumple?
            (disyuncion (Dato. 4) (Dato. 3) (Dato. 1))
            (partial > 5)
        ))
    )
    (testing "datos < 5 y uno > 5 no cumplen conjuncion"
        (is (not (cumple?
            (conjuncion (Dato. 8) (Dato. 3) (Dato. 1))
            (partial > 5)
        )))
    )
    (testing "datos < 5 y uno > 5 cumplen disyuncion"
        (is (cumple?
            (disyuncion (Dato. 4) (Dato. 70000) (Dato. 1))
            (partial > 5)
        ))
    )
)

(deftest depende-del-criterio
    (let [
        dis-100-50-20 (disyuncion (Dato. 100) (Dato. 50) (Dato. 20))
        con-100-50-20 (conjuncion (Dato. 100) (Dato. 50) (Dato. 20))
    ]
        (testing "menores a 100, pasan ambos"
            (is (cumple? dis-100-50-20 (partial >= 100)))
            (is (cumple? con-100-50-20 (partial >= 100)))
        )
        (testing "menores a 30, pasa disyuncion"
            (is (cumple? dis-100-50-20 (partial >= 30)))
            (is (not (cumple? con-100-50-20 (partial >= 30))) )
        )
        (testing "menores a 10, pasa ninguno"
            (is (not (cumple? dis-100-50-20 (partial >= 10))) )
            (is (not (cumple? con-100-50-20 (partial >= 10))) )
        )
    )
)

(deftest composicion
    (let [
        dis-100-50-20 (disyuncion (Dato. 100) (Dato. 50) (Dato. 20))
        con-100-50-20 (conjuncion (Dato. 100) (Dato. 50) (Dato. 20))
        super-con (conjuncion dis-100-50-20 con-100-50-20)
        super-dis (disyuncion dis-100-50-20 con-100-50-20)
        
    ]
        (testing "menores a 100, pasan ambos"
            (is (cumple? super-con (partial >= 100)))
            (is (cumple? super-dis (partial >= 100)))
        )
        (testing "menores a 30, pasa disyuncion"
            (is (cumple? super-dis (partial >= 30)))
            (is (not (cumple? super-con (partial >= 30))) )
        )
        (testing "menores a 10, pasa ninguno"
            (is (not (cumple? super-dis (partial >= 10))) )
            (is (not (cumple? super-con (partial >= 10))) )
        )
    )
)
