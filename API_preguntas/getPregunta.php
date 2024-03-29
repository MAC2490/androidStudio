<?php 
	header("Access-Control-Allow-Origin: * "); // Permite el acceso desde cualquier origen, o usa "http://localhost" si solo quieres permitirlo desde localhost.
	header("Access-Control-Allow-Methods: GET, POST");
	header("Access-Control-Allow-Headers: Content-Type");
    include 'Conexion.php';
    if (!empty($_POST['id_pregunta'])) {
        $id_pregunta = $_POST['id_pregunta'];
        $consulta = $base_de_datos->prepare("SELECT preguntas.id, preguntas.descripcion, preguntas.id_correcta, preguntas.url_imagen
                                                        FROM preguntas 
                                                        WHERE id = :id");
        $consulta->bindParam(':id', $id_pregunta);
        $consulta->execute();
        $pregunta = $consulta->fetch(PDO::FETCH_ASSOC);

        $respuesta = [
            'status' => true
        ];

        if($pregunta){
            $opciones = $base_de_datos->prepare("SELECT * FROM opciones WHERE id_pregunta= :id_resp");
            $opciones->bindParam(':id_resp', $id_pregunta);
            $opciones->execute();
            $opcion = $opciones->fetchAll(PDO::FETCH_ASSOC);

            $respuesta['respuesta'] = [
                'pregunta' => $pregunta,
                'opciones' => $opcion
            ];
        }  
        echo json_encode($respuesta);
	}else{
        $respuesta = [
                        'status' => false,
                        'mesagge' => "ERROR##DATOS##GET",
                        'POST' => $_POST
                    ];
        echo json_encode($respuesta);
    }
?>