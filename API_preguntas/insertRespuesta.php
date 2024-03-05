<?php
    include "Conexion.php";

    if (!empty($_POST['id_cuestionario']) && !empty($_POST['id_pregunta']) && !empty($_POST['descripcion']) && !empty($_POST['estado']) && !empty($_POST['fecha_inicio'])) {

        $id_cuestionario = $_POST['id_cuestionario'];
        $id_pregunta = $_POST['id_pregunta'];
        $descripcion = $_POST['descripcion'];
        $estado = $_POST['estado'];
        $fecha_inicio = $_POST['fecha_inicio'];

        try {
            $insert = $base_de_datos->prepare("INSERT INTO respuestas (id_cuestionario, id_pregunta, respuesta, estado, fecha) VALUES(:idC, :idP, :idD, :idE, :idF)");

            $insert->bindParam(':idC', $id_cuestionario);
            $insert->bindParam(':idP', $id_pregunta);
            $insert->bindParam(':idD', $descripcion);
            $insert->bindParam(':idE', $estado);
            $insert->bindParam(':idF', $fecha_inicio);
            $insert->execute();

            if( $insert ){
                if ($estado == 'OK') {
                    $update = $base_de_datos->prepare("UPDATE cuestionarios SET cant_preguntas = cant_preguntas + 1, cant_ok = cant_ok + 1 WHERE id = $id_cuestionario");
                    $update->execute();
                }else{
                    $update = $base_de_datos->prepare("UPDATE cuestionarios SET cant_preguntas = cant_preguntas + 1, cant_error = cant_error + 1 WHERE id = $id_cuestionario");
                    $update->execute();
                }
                if ($insert) {
                    $respuesta = [
                                    'status' => true,
                                    'mesagge' => "OK##CLIENT##INSERT",
                                    'mesaggeUpdate' => "OK##CLIENT##UPDATE", 
                                  ];
                    echo json_encode($respuesta);
                }else{  
                    $respuesta = [
                        'status' => false,
                        'mesagge' => "ERROR##CLIENT##UPDATE"
                      ];
                }
            }else{
                $respuesta = [
                                'status' => false,
                                'mesagge' => "ERROR##CLIENT##INSERT"
                              ];
                echo json_encode($respuesta);
            }
        } catch (Exception $e) {
            $respuesta = [
                            'status' => false,
                            'mesagge' => "ERROR##SQL",
                            'exception' => $e,
                            'datos' => $_POST
                        ];
            echo json_encode($respuesta);
        }
    }else{
        $respuesta = [
                        'status' => false,
                        'mesagge' => "ERROR##DATOS##POST",
                    ];
        echo json_encode($respuesta);
    }
?>