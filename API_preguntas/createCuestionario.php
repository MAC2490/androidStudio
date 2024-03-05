<?php
    include "Conexion.php";

    if (!empty($_POST['id_usuario'])) {

        $id_usuario = $_POST['id_usuario'];

        try {
            $insert = $base_de_datos->prepare("INSERT INTO cuestionarios (id_usuario, fecha_inicio) VALUES(:id, CURRENT_TIMESTAMP())");

            $insert->bindParam(':id', $id_usuario);
            $insert->execute();

            if( $insert ){
                $consulta = $base_de_datos->prepare("SELECT id FROM cuestionarios WHERE fecha_inicio = CURRENT_TIMESTAMP()");
                $consulta->execute();
                $cuestionario = $consulta->fetchAll(PDO::FETCH_ASSOC);
                $respuesta = [
                                'status' => true,
                                'mesagge' => "OK##CLIENT##INSERT",
                                'id_usuario' => $id_usuario,
                                'id_cuestionario' => $cuestionario[0]
                              ];
                echo json_encode($respuesta);
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
                            'exception' => $e
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