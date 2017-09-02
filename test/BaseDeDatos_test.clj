(ns BaseDeDatos-test
    (:require 
        [clojure.test :refer :all]
        [BaseDeDatos :refer :all]
        [Premisa :as p]
    )
)

(deftest solo-una-premisa
    (testing "un string que solo tiene premisas"
      (let [
          base (parsearBaseDeDatos "cumple(jeronimo).")
      ]
        (is (verifica? base (p/parsear "cumple(jeronimo)")) )
        (is (not (verifica? base (p/parsear "cumple(juan)"))))
      )
    )
)

(deftest muchas-premisas
    (testing "un string que solo tiene premisas"
      (let [
          base (parsearBaseDeDatos "
          cumple(jeronimo).\n
          cumple(juan).\n\n\n\n\n
          cum\nple(arquimedes).\n
          cumple(newton).\n
          cumple(juan\n\n\n\n\ncito).")
      ]
        (is (verifica? base (p/parsear "cumple(jeronimo)")) )
        (is (verifica? base (p/parsear "cumple(juan)")) )
        (is (verifica? base (p/parsear "cumple(arquimedes)")) )
        (is (verifica? base (p/parsear "cumple(newton)")) )
        (is (verifica? base (p/parsear "cumple(juancito)")) )
        (is (not (verifica? base (p/parsear "cumple(X)")) ))
        (is (not (verifica? base (p/parsear "cumple(Juan)")) ))
        (is (not (verifica? base (p/parsear "cumple(Juan)")) ))
      )
    )
)

(deftest inferencias
    (testing "un string con inferencias"
      (let [
          base (parsearBaseDeDatos "
          cumple(jeronimo).\n
          cumple(juan).\n\n\n\n\n
          cum\nple(arquimedes).\n
          cumple(newton).\n
          cumple(juan\n\n\n\n\ncito).
          hace(juan).
          hace(perez).
          hace(arquimedes).
          hara(X):-hace(X).
          cumplira(X):-cumple(X).
          bueno(X):-hace(X);cumple(X).
          buenosbuenosbuenos(X):-bueno(X);cumplira(X).")
      ]
      
      (is (verifica? base (p/parsear "hara(juan)")) )
      (is (verifica? base (p/parsear "hara(perez)")) )
      (is (not (verifica? base (p/parsear "hara(Emanuel)")) ) )

      (is (verifica? base (p/parsear "bueno(perez)")) )
      (is (verifica? base (p/parsear "bueno(juancito)")) )
      (is (not (verifica? base (p/parsear "bueno(Miguelito)")) ) )

      (is (verifica? base (p/parsear "buenosbuenosbuenos(juancito)")) )
      (is (not (verifica? base (p/parsear "buenosbuenosbuenos(Mafalda)")) ) )
      (is (verifica? base (p/parsear "buenosbuenosbuenos(perez)")) )
      )
    )
)

(deftest inferencias-aridad-mayor
    (testing "un string con inferencias y mas aridad"
      (let [
          base (parsearBaseDeDatos "
          amigos(andres,berto).
          amigos(andres,carton).
          amigos(berto,carton).
          amigos(juan,jero,japon,jalea,jonas,job,jose,jeremias,julian).
          amigos(X,Y):-amigos(Y,X).
          amigos(X,Y,Z):-amigos(X,Y);amigos(Y,Z);amigos(X,Z).")
      ]
      
        (is (verifica? base (p/parsear "amigos(berto,andres)")))
        (is (verifica? base (p/parsear "amigos(berto,andres,ximena)")))
        (is (verifica? base (p/parsear "amigos(berto,juan,andres)")))
      )
    )
)

(deftest pequenio-error-punto
    (testing "olvido un punto"
      (let [
          base (parsearBaseDeDatos "
          cumple(jeronimo).\n
          cumple(juan).\n\n\n\n\n
          cum\nple(arquimedes).\n
          cumple(newton).\n
          cumple(juan\n\n\n\n\ncito).
          hace(juan)
          hace(perez).
          hace(arquimedes).
          hara(X):-hace(X).
          cumplira(X):-cumple(X).
          bueno(X):-hace(X);cumple(X).
          buenosbuenosbuenos(X):-bueno(X);cumplira(X).")
      ]
      (is (= base nil))
      )
    )
)

(deftest pequenio-error-parentesis
    (testing "olvido un parentesis"
      (let [
          base (parsearBaseDeDatos "
          cumple(jeronimo).\n
          cumple(juan).\n\n\n\n\n
          cum\nple(arquimedes).\n
          cumple(newton).\n
          cumple(juan\n\n\n\n\ncito).
          hace(juan.
          hace(perez).
          hace(arquimedes).
          hara(X):-hace(X).
          cumplira(X):-cumple(X).
          bueno(X):-hace(X);cumple(X).
          buenosbuenosbuenos(X):-bueno(X);cumplira(X).")
      ]
      (is (= base nil))
      )
    )
)

(deftest pequenio-error-dospuntos
    (testing "olvido un dospuntos"
      (let [
          base (parsearBaseDeDatos "
          cumple(jeronimo).\n
          cumple(juan).\n\n\n\n\n
          cum\nple(arquimedes).\n
          cumple(newton).\n
          cumple(juan\n\n\n\n\ncito).
          hace(juan).
          hace(perez).
          hace(arquimedes).
          hara(X)-hace(X).
          cumplira(X):-cumple(X).
          bueno(X):-hace(X);cumple(X).
          buenosbuenosbuenos(X):-bueno(X);cumplira(X).")
      ]
      (is (= base nil))
      )
    )
)

(deftest pequenio-error-guion
    (testing "olvido un guion"
      (let [
          base (parsearBaseDeDatos "
          cumple(jeronimo).\n
          cumple(juan).\n\n\n\n\n
          cum\nple(arquimedes).\n
          cumple(newton).\n
          cumple(juan\n\n\n\n\ncito).
          hace(juan).
          hace(perez).
          hace(arquimedes).
          hara(X):hace(X).
          cumplira(X):-cumple(X).
          bueno(X):-hace(X);cumple(X).
          buenosbuenosbuenos(X):-bueno(X);cumplira(X).")
      ]
      (is (= base nil))
      )
    )
)

(deftest pequenio-error-ptoycoma
    (testing "olvido un pto y coma"
      (let [
          base (parsearBaseDeDatos "
          cumple(jeronimo).\n
          cumple(juan).\n\n\n\n\n
          cum\nple(arquimedes).\n
          cumple(newton).\n
          cumple(juan\n\n\n\n\ncito).
          hace(juan).
          hace(perez).
          hace(arquimedes).
          hara(X):-hace(X).
          cumplira(X):-cumple(X).
          bueno(X):-hace(X)cumple(X).
          buenosbuenosbuenos(X):-bueno(X);cumplira(X).")
      ]
      (is (= base nil))
      )
    )
)

(deftest cualquier-cosa
    (testing "olvido un pto y coma"
      (let [
          base (parsearBaseDeDatos "
          Hace mucho(mucho). mucho tiempo
          que no me dedico a poner bien(y,q):-hola(y).p[a'arrafos a las cosas \n\n\n\n\n
          Porque\n aunque quisiera no podria. hola(x,y).chau(p,s).")
      ]
      (is (= base nil))
      )
    )
)