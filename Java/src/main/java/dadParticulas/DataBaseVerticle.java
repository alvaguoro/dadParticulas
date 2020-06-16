package dadParticulas;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import tipos.Actuador;
import tipos.ActuadorAlarmaValue;
import tipos.ActuadorLcdValue;
import tipos.Device;
import tipos.Sensor;
import tipos.Sensor_DHT11_value;
import tipos.Sensor_particula_value;
import tipos.Usuario;

public class DataBaseVerticle extends AbstractVerticle{

	public static Boolean flag=false;
	
		
	
	public static Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		DataBaseVerticle.flag = flag;
	}

	private MySQLPool mySQLPool;	
	@Override
	public void start(Promise<Void> startPromise) {
		MySQLConnectOptions mySQLConnectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("particulas").setUser("root").setPassword("MEllamoeduar");
		PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
		mySQLPool = MySQLPool.pool(vertx, mySQLConnectOptions, poolOptions);
		Router router = Router.router(vertx);
		router.route().handler(BodyHandler.create());
		vertx.createHttpServer().requestHandler(router::handle).listen(8082, result -> {
			if (result.succeeded()) {
				startPromise.complete();
			}else {
				startPromise.fail(result.cause());
			}
		});
		
		router.get("/api/device/:idDevice").handler(this::getDevice);
		router.put("/api/device").handler(this::putDevice);
		router.delete("/api/device/:idDevice").handler(this::deleteDeviceById);
		
		router.get("/api/sensor/:idSensor").handler(this::getSensorById);
		router.put("/api/sensor").handler(this::putSensor);
		router.delete("/api/sensor/:idSensor").handler(this::deleteSensorById);
		
		router.get("/api/actuador/:idActuador").handler(this::getActuadorById);
		router.put("/api/actuador").handler(this::putActuador);
		router.delete("/api/actuador/:idActuador").handler(this::deleteActuadorById);
		
		router.route("/api/actuador/actuadorAlarmaValue*").handler(BodyHandler.create());
		router.get("/api/actuador/actuadorAlarmaValue/:idActuador").handler(this::getActuadorAlarmaValueByIdLast);
		router.put("/api/actuador/actuadorAlarmaValue").handler(this::putActuadorAlarmaValue);
		router.delete("/api/actuador/actuadorAlarmaValue/:idActuador_alarma_value").handler(this::deleteActuadorAlarmaValueById);
		
		
		router.route("/api/sensor/particulas_values*").handler(BodyHandler.create());
		router.get("/api/sensor/particulas_values/:idSensor").handler(this::getSensorParticulaValuePorSensorLast);
		router.put("/api/sensor/particulas_values").handler(this::putSensorParticulaValue);
		router.delete("/api/sensor/particulas_values/:idSensor_particula_value").handler(this::deleteSensorParticulaValueById);

		router.route("/api/sensor/DHT11_values*").handler(BodyHandler.create());
		router.get("/api/sensor/DHT11_values/:idSensor").handler(this::getSensorDHT11ValuePorSensorLast);
        router.put("/api/sensor/DHT11_values").handler(this::putSensorDHT11Value);
		router.delete("/api/sensor/DHT11_values/:idSensor_DHT11_value").handler(this::deleteSensorDHT11ValueById);
        
		router.delete("/api/restaurarValues").handler(this::deleteTables);
		
		
		//Codigo LCD que se podria implementar
		//router.get("/api/actuador/actuadorLcdValue/:idActuador_LCD_value").handler(this::getActuadorLcdValueById);
		//router.put("/api/actuador/actuadorLcdValue").handler(this::putActuadorLcdValue);
		//router.delete("/api/actuador/actuadorLcdValue/:idActuador_lcd_value").handler(this::deleteActuadorLcdValueById);
				
		
		//No lo vimos necesario para nuestra implementacion
		//router.get("/api/usuario/:idUsuario").handler(this::getUsuario);
		//router.put("/api/usuario").handler(this::putUsuario);
		//router.delete("/api/usuario/:idUsuario").handler(this::deleteUsuarioById);     
	}
	
	//
	//DEVICE/////////////////////////////////////////////
	//
	private void getDevice(RoutingContext routingContext) {
		mySQLPool.query("SELECT * FROM particulas.device WHERE idDevice= " + routingContext.request().getParam("idDevice"), 
				res->{
					if(res.succeeded()) {
						RowSet<Row> resultSet= res.result();
						System.out.println("El numero de elementos obtenidos es "+resultSet.size());
						JsonArray result=new JsonArray();
						for(Row row : resultSet) {
                            result.add(JsonObject.mapFrom(new Device(
                                    row.getInteger("idDevice"),
                                    row.getString("ip"),
                                    row.getString("name"),
                                    row.getInteger("idUsuario"),
                                    row.getLong("InitialTimestamp"),
                                    row.getString("ubicacion"))));
                        
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
                        .end(result.encodePrettily());
					}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "aplication/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
		
	private void putDevice(RoutingContext routingContext) {
		Device dev = Json.decodeValue(routingContext.getBodyAsString(), Device.class);
		mySQLPool.preparedQuery("INSERT INTO particulas.device(ip, name, idUsuario, initialTimestamp, ubicacion) VALUES (?,?,?,?,?)", 
				Tuple.of(dev.getIp(), dev.getName(), dev.getIdUsuario(), dev.getInitialTimestamp(), dev.getUbicacion()), handler -> {
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
								
								long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
								dev.setIdDevice((int) id);
								
								routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(dev).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
    }
	
	private void deleteDeviceById(RoutingContext routingContext) {
		String id = routingContext.request().getParam("idDevice");
		mySQLPool.query("DELETE FROM particulas.device WHERE idDevice = " + routingContext.request().getParam("idDevice"),
				res -> {
					if(res.succeeded()) {
						routingContext.response().setStatusCode(200).end("Borrado Device con id=" + id);
					}else {
						System.out.println(res.cause().toString());
						routingContext.response().setStatusCode(401).end("No se ha borrado el Device con id=" + id);
					}
				});
	}
	
	
	
	//
	//SENSOR/////////////////////////////////////////////
	//
	private void getSensorById(RoutingContext routingContext) {
		mySQLPool.query("SELECT * FROM particulas.sensor WHERE idSensor = " + routingContext.request().getParam("idSensor"), 
				res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El numero de elementos obtenidos es " + resultSet.size());
						JsonArray result = new JsonArray();
						for(Row row : resultSet) {
							result.add(JsonObject.mapFrom(new Sensor(
									row.getInteger("idSensor"),
									row.getString("type"),
									row.getString("name"),
									row.getInteger("idDevice"),
									row.getString("ubicacion"))));
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end(result.encodePrettily());
					}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}	
				});
	}
	
	
	private void putSensor(RoutingContext routingContext) {
		Sensor sensor = Json.decodeValue(routingContext.getBodyAsString(), Sensor.class);
		mySQLPool.preparedQuery("INSERT INTO particulas.sensor(type, name, idDevice, ubicacion) VALUES (?,?,?,?)", 
				Tuple.of(sensor.getType(), sensor.getName(), sensor.getIdDevice(),
						sensor.getUbicacion()), handler -> {
							if (handler.succeeded()) {
								System.out.println(handler.result().rowCount());
								
								long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
								sensor.setIdSensor((int) id);
								
								routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(sensor).encodePrettily());
							}else {
								System.out.println(handler.cause().toString());
								routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
							}
						});
	}
	
	private void deleteSensorById(RoutingContext routingContext) {
		String id = routingContext.request().getParam("idSensor");
		mySQLPool.query("DELETE FROM particulas.sensor WHERE idSensor = " + routingContext.request().getParam("idSensor"),
				res -> {
					if(res.succeeded()) {
						routingContext.response().setStatusCode(200).end("Borrado Sensor con id=" + id);
					}else {
						System.out.println(res.cause().toString());
						routingContext.response().setStatusCode(401).end("No se ha borrado el Sensor con id=" + id);
					}
				});
	}
	
	//
	//ACTUADOR/////////////////////////////////////////////
	///
	
	private void getActuadorById(RoutingContext routingContext) {
		mySQLPool.query("SELECT * FROM particulas.actuador WHERE idActuador = " + routingContext.request().getParam("idActuador"), 
				res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El numero de elementos obtenidos es " + resultSet.size());
						JsonArray result = new JsonArray();
						for(Row row : resultSet) {
							result.add(JsonObject.mapFrom(new Actuador(
									row.getInteger("idActuador"),
									row.getString("type"),
									row.getString("name"),
									row.getInteger("idDevice"),
									row.getString("ubicacion"))));
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end(result.encodePrettily());
					}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}	
				});
	}
	
	private void putActuador(RoutingContext routingContext) {
		Actuador actuador = Json.decodeValue(routingContext.getBodyAsString(), Actuador.class);
		mySQLPool.preparedQuery("INSERT INTO particulas.actuador(type, name, idDevice, ubicacion) VALUES (?,?,?,?)", 
				Tuple.of(actuador.getType(), actuador.getName(), actuador.getIdDevice(), actuador.getUbicacion()), handler -> {
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
								
								long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
								actuador.setIdActuador((int) id);
								
								routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(actuador).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	private void deleteActuadorById(RoutingContext routingContext) {
		String id = routingContext.request().getParam("idActuador");
		mySQLPool.query("DELETE FROM particulas.actuador WHERE idActuador = " + routingContext.request().getParam("idActuador"),
				res -> {
					if(res.succeeded()) {
						routingContext.response().setStatusCode(200).end("Borrado Actuador con id=" + id);
					}else {
						System.out.println(res.cause().toString());
						routingContext.response().setStatusCode(401).end("No se ha borrado el Actuador con id=" + id);
					}
				});
	}
	
	
	//
	//ACTUADOR ALARMA/////////////////////////////////////////////
	///
	private void getActuadorAlarmaValueByIdLast(RoutingContext routingContext) {
		mySQLPool.query("SELECT * FROM particulas.actuador_alarma_value WHERE idActuador =" + routingContext.request().getParam("idActuador")
				+ " ORDER BY idActuador_alarma_value DESC LIMIT 1",
		//mySQLPool.query("SELECT * FROM particulas.actuador_alarma_value WHERE idActuador_alarma_value = " + routingContext.request().getParam("idActuador_alarma_value"), 
				res -> {
					if (res.succeeded()) {
						RowSet<Row> resultSet = res.result();
						System.out.println("El numero de elementos obtenidos es " + resultSet.size());
						JsonArray result = new JsonArray();
						for(Row row : resultSet) {
							result.add(JsonObject.mapFrom(new ActuadorAlarmaValue(
									row.getInteger("idActuador_alarma_value"),
									row.getInteger("idActuador"),
									row.getFloat("value"),
									row.getLong("timestamp"))));
						}
						
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
							.end(result.encodePrettily());
					}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}	
				});
	}
	
	private void putActuadorAlarmaValue(RoutingContext routingContext) {
		ActuadorAlarmaValue actuadorAlarmaValue = Json.decodeValue(routingContext.getBodyAsString(), ActuadorAlarmaValue.class);
		mySQLPool.preparedQuery("INSERT INTO particulas.actuador_alarma_value(idActuador, value, timestamp) VALUES (?,?,?)", 
				Tuple.of(actuadorAlarmaValue.getIdActuador(), actuadorAlarmaValue.getValue(), actuadorAlarmaValue.getTimestamp()), handler -> {
					if(handler.succeeded()) {
						System.out.println(handler.result().rowCount());
								
								long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
								actuadorAlarmaValue.setIdActuador_alarma_value((int) id);
								if(actuadorAlarmaValue.getValue()==2.0) {
									flag=true;
								}else {
									flag=false;
								}
								routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(actuadorAlarmaValue).encodePrettily());
					}else {
						System.out.println(handler.cause().toString());
						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
					}
				});
	}
	
	private void deleteActuadorAlarmaValueById(RoutingContext routingContext) {
		String id = routingContext.request().getParam("idActuador_alarma_value");
		mySQLPool.query("DELETE FROM particulas.actuador_alarma_value WHERE idActuador_alarma_value = " + routingContext.request().getParam("idActuador_alarma_value"),
				res -> {
					if(res.succeeded()) {
						routingContext.response().setStatusCode(200).end("Borrado ActuadorAlarmaValue con id=" + id);
					}else {
						System.out.println(res.cause().toString());
						routingContext.response().setStatusCode(401).end("No se ha borrado el ActuadorAlarmaValue con id=" + id);
					}
				});
	}
	
	
	
	//
	//SENSOR PARTICULAS/////////////////////////////////////////////
	///
	
	
	
	private void getSensorParticulaValuePorSensorLast(RoutingContext routingContext) {
        mySQLPool.query("SELECT * FROM particulas.sensor_particula_value WHERE idSensor= " + routingContext.request().getParam("idSensor")
        		+ " ORDER BY idSensor_particula_value DESC LIMIT 1", 
                res->{
                    if(res.succeeded()) {
                        RowSet<Row> resultSet= res.result();
                        System.out.println("El numero de elementos obtenidos es "+resultSet.size());
                        JsonArray result=new JsonArray();
                        for(Row row : resultSet) {
                            result.add(JsonObject.mapFrom(new Sensor_particula_value(
                                    row.getInteger("idSensor_particula_value"),
                                    row.getInteger("idSensor"),
                                    row.getFloat("Particulas_1"),
                                    row.getFloat("Particulas_25"),
                                    row.getFloat("Particulas_10"),
                                    row.getFloat("accuracy"),
                                    row.getLong("timestamp"))));

                        }
                        routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
                        .end(result.encodePrettily());
                    }else {
                        routingContext.response().setStatusCode(401).putHeader("content-type", "aplication/json")
                        .end((JsonObject.mapFrom(res.cause()).encodePrettily()));
                    }
                });
    }
	
	private void putSensorParticulaValue(RoutingContext routingContext) {
        Sensor_particula_value sensorParticulaValue = Json.decodeValue(routingContext.getBodyAsString(), Sensor_particula_value.class);
        mySQLPool.preparedQuery("INSERT INTO particulas.sensor_particula_value(idSensor, Particulas_1, Particulas_25, Particulas_10, accuracy, timestamp) VALUES (?,?,?,?,?,?)", 
                Tuple.of(sensorParticulaValue.getIdSensor(), sensorParticulaValue.getParticulas_1(), sensorParticulaValue.getParticulas_25(),
                        sensorParticulaValue.getParticulas_10(), sensorParticulaValue.getAccuracy(), sensorParticulaValue.getTimestamp()), handler -> {
                            if (handler.succeeded()) {
                                System.out.println(handler.result().rowCount());

                                long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
                                sensorParticulaValue.setIdSensor_particula_value((int) id);

                                routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
                                .end(JsonObject.mapFrom(sensorParticulaValue).encodePrettily());
                            }else {
                                System.out.println(handler.cause().toString());
                                routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
                                .end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
                            }
                        });
    }
	
	private void deleteSensorParticulaValueById(RoutingContext routingContext) {
		String id = routingContext.request().getParam("idSensor_particula_value");
		mySQLPool.query("DELETE FROM particulas.sensor_particula_value WHERE idSensor_particula_value = " + routingContext.request().getParam("idSensor_particula_value"),
				res -> {
					if(res.succeeded()) {
						routingContext.response().setStatusCode(200).end("Borrado SensorParticulaValue con id=" + id);
					}else {
						System.out.println(res.cause().toString());
						routingContext.response().setStatusCode(401).end("No se ha borrado el SensorParticulaValue con id=" + id);
					}
				});
	}
	
	
	
	//
	//SENSOR DHT11/////////////////////////////////////////////
	///
	
	
	
	
	private void getSensorDHT11ValuePorSensorLast(RoutingContext routingContext) {
		mySQLPool.query("SELECT * FROM particulas.sensor_dht11_value WHERE idSensor= " + routingContext.request().getParam("idSensor") 
				+ " ORDER BY idSensor_DHT11_value DESC LIMIT 1",
				res->{
					if(res.succeeded()) {
						RowSet<Row> resultSet= res.result();
						System.out.println("El numero de elementos obtenidos es "+resultSet.size());
						JsonArray result=new JsonArray();
						for(Row row : resultSet) {
                            result.add(JsonObject.mapFrom(new Sensor_DHT11_value(
                            		row.getInteger("idSensor_DHT11_value"),
                            		row.getInteger("idSensor"),
                            		row.getFloat("Temperatura"),
                            		row.getFloat("Humedad"),
                            		row.getFloat("accuracy"),
                                    row.getLong("timestamp"))));
                        
						}
						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
                        .end(result.encodePrettily());
					}else {
						routingContext.response().setStatusCode(401).putHeader("content-type", "aplication/json")
						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
					}
				});
	}
	
	private void putSensorDHT11Value(RoutingContext routingContext) {
		Sensor_DHT11_value sensorDHT11Value = Json.decodeValue(routingContext.getBodyAsString(), Sensor_DHT11_value.class);
		mySQLPool.preparedQuery("INSERT INTO particulas.sensor_DHT11_value(idSensor, Temperatura, Humedad, accuracy, timestamp) VALUES (?,?,?,?,?)", 
				Tuple.of(sensorDHT11Value.getIdSensor(), sensorDHT11Value.getTemperatura(), sensorDHT11Value.getHumedad(),
						sensorDHT11Value.getAccuracy(), sensorDHT11Value.getTimestamp()), handler -> {
							if (handler.succeeded()) {
								System.out.println(handler.result().rowCount());
								
								long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
								sensorDHT11Value.setIdSensor_DHT11_value((int) id);
								
								routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
								.end(JsonObject.mapFrom(sensorDHT11Value).encodePrettily());
							}else {
								System.out.println(handler.cause().toString());
								routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
								.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
							}
						});
	}
	
	private void deleteSensorDHT11ValueById(RoutingContext routingContext) {
		String id = routingContext.request().getParam("idSensor_DHT11_value");
		mySQLPool.query("DELETE FROM particulas.sensor_DHT11_value WHERE idSensor_particula_value = " + routingContext.request().getParam("idSensor_DHT11_value"),
				res -> {
					if(res.succeeded()) {
						routingContext.response().setStatusCode(200).end("Borrado SensorDHT11Value con id=" + id);
					}else {
						System.out.println(res.cause().toString());
						routingContext.response().setStatusCode(401).end("No se ha borrado el SensorDHT11Value con id=" + id);
					}
				});
	}
	
	
	
	private void deleteTables(RoutingContext routingContext) {
		
		mySQLPool.query("DELETE FROM particulas.sensor_DHT11_value ",
				res -> {
					if(res.succeeded()) {
						routingContext.response().setStatusCode(200).end("Borrado de tablas");
					}else {
						System.out.println(res.cause().toString());
						routingContext.response().setStatusCode(401).end("No se ha borrado" );
					}
				});
		mySQLPool.query("DELETE FROM particulas.sensor_particula_value ",
				res -> {
					if(res.succeeded()) {
						routingContext.response().setStatusCode(200).end("Borrado de tablas");
					}else {
						System.out.println(res.cause().toString());
						routingContext.response().setStatusCode(401).end("No se ha borrado ");
					}
				});
		mySQLPool.query("DELETE FROM particulas.actuador_alarma_value ",
				res -> {
					if(res.succeeded()) {
						routingContext.response().setStatusCode(200).end("Borrado de tablas");
					}else {
						System.out.println(res.cause().toString());
						routingContext.response().setStatusCode(401).end("No se ha borrado ");
					}
				});
	}
	
	
//	private void getUsuario(RoutingContext routingContext) {
//		mySQLPool.query("SELECT * FROM particulas.usuario WHERE idUsuario= " + routingContext.request().getParam("idUsuario"), 
//				res->{
//					if(res.succeeded()) {
//						RowSet<Row> resultSet= res.result();
//						System.out.println("El numero de elementos obtenidos es "+resultSet.size());
//						JsonArray result=new JsonArray();
//						for(Row row : resultSet) {
//                            result.add(JsonObject.mapFrom(new Usuario(
//                                    row.getInteger("idUsuario"),
//                                    row.getString("type"),
//                                    row.getString("name"),
//                                    row.getString("surname"),
//                                    row.getString("dni"),
//                                    row.getLong("fechaNacimiento"))));
//                        
//						}
//						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
//                        .end(result.encodePrettily());
//					}else {
//						routingContext.response().setStatusCode(401).putHeader("content-type", "aplication/json")
//						.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
//					}
//				});
//	}
//	
//	private void putUsuario(RoutingContext routingContext) {
//		Usuario usuario = Json.decodeValue(routingContext.getBodyAsString(), Usuario.class);
//        mySQLPool.preparedQuery("INSERT INTO particulas.usuario(type, name, surname, dni, fechaNacimiento) VALUES (?,?,?,?,?)", 
//        		Tuple.of(usuario.getType(), usuario.getName(), usuario.getSurname(), usuario.getDni(), usuario.getFechaNacimiento()), handler -> {
//         
//                    if(handler.succeeded()) {
//                        System.out.println(handler.result().rowCount());
//
//                                long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
//                                usuario.setIdUsuario((int) id);
//
//                                routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
//                                .end(JsonObject.mapFrom(usuario).encodePrettily());
//                    }else {
//                        System.out.println(handler.cause().toString());
//                        routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
//                        .end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
//                    }
//                });
//    }
//	
//	private void deleteUsuarioById(RoutingContext routingContext) {
//		String id = routingContext.request().getParam("idUsuario");
//		mySQLPool.query("DELETE FROM particulas.usuario WHERE idUsuario = " + routingContext.request().getParam("idUsuario"),
//				res -> {
//					if(res.succeeded()) {
//						routingContext.response().setStatusCode(200).end("Borrado Usuario con id=" + id);
//					}else {
//						System.out.println(res.cause().toString());
//						routingContext.response().setStatusCode(401).end("No se ha borrado el Usuario con id=" + id);
//					}
//				});
//	}
//	
//	private void getActuadorLcdValueById(RoutingContext routingContext) {
//		mySQLPool.query("SELECT * FROM particulas.actuador_lcd_value WHERE idActuador_LCD_value = " + routingContext.request().getParam("idActuador_LCD_value"), 
//				res -> {
//					if (res.succeeded()) {
//						RowSet<Row> resultSet = res.result();
//						System.out.println("El numero de elementos obtenidos es " + resultSet.size());
//						JsonArray result = new JsonArray();
//						for(Row row : resultSet) {
//							result.add(JsonObject.mapFrom(new ActuadorLcdValue(
//									row.getInteger("idActuador_LCD_value"),
//									row.getInteger("idActuador"),
//									row.getString("mensaje"),
//									row.getLong("timestamp"))));
//						}
//						
//						routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
//							.end(result.encodePrettily());
//					}else {
//						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
//							.end((JsonObject.mapFrom(res.cause()).encodePrettily()));
//					}	
//				});
//	}
//	
//	private void putActuadorLcdValue(RoutingContext routingContext) {
//		ActuadorLcdValue actuadorLcdValue = Json.decodeValue(routingContext.getBodyAsString(), ActuadorLcdValue.class);
//		mySQLPool.preparedQuery("INSERT INTO particulas.actuador_lcd_value(idActuador, mensaje, timestamp) VALUES (?,?,?)", 
//				Tuple.of(actuadorLcdValue.getIdActuador(), actuadorLcdValue.getMensaje(), actuadorLcdValue.getTimestamp()), handler -> {
//					if(handler.succeeded()) {
//						System.out.println(handler.result().rowCount());
//								
//								long id = handler.result().property(MySQLClient.LAST_INSERTED_ID);
//								actuadorLcdValue.setIdActuador_LCD_value((int) id);
//								
//								routingContext.response().setStatusCode(200).putHeader("content-type", "application/json")
//								.end(JsonObject.mapFrom(actuadorLcdValue).encodePrettily());
//					}else {
//						System.out.println(handler.cause().toString());
//						routingContext.response().setStatusCode(401).putHeader("content-type", "application/json")
//						.end((JsonObject.mapFrom(handler.cause()).encodePrettily()));
//					}
//				});
//	}
//	
//	private void deleteActuadorLcdValueById(RoutingContext routingContext) {
//		String id = routingContext.request().getParam("idActuador_lcd_value");
//		mySQLPool.query("DELETE FROM particulas.actuador_lcd_value WHERE idActuador_lcd_value = " + routingContext.request().getParam("idActuador_Lcd_value"),
//				res -> {
//					if(res.succeeded()) {
//						routingContext.response().setStatusCode(200).end("Borrado ActuadorLcdValue con id=" + id);
//					}else {
//						System.out.println(res.cause().toString());
//						routingContext.response().setStatusCode(401).end("No se ha borrado el ActuadorLcdValue con id=" + id);
//					}
//				});
//	}
	
}
