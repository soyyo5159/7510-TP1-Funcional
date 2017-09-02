(ns tratar-input)

(defn tratar-input
    "interactua con el usuario"
    [leer mostrar reponder-pregunta]
    (mostrar "Ingrese una nueva pregunta o q para salir")
    (let [lectura (leer)]
      (if (not (= lectura "q") )
        (do 
          (mostrar (reponder-pregunta lectura))
          (tratar-input leer mostrar reponder-pregunta)
        )
        "fin"
      )
    )
  )