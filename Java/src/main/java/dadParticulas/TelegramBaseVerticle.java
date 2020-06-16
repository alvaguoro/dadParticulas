package dadParticulas;

import org.schors.vertx.telegram.bot.LongPollingReceiver;
import org.schors.vertx.telegram.bot.TelegramBot;
import org.schors.vertx.telegram.bot.TelegramOptions;
import org.schors.vertx.telegram.bot.api.methods.SendMessage;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
public class TelegramBaseVerticle extends AbstractVerticle {
		 
		private TelegramBot bot;
		private Float Temperatura;
		private Float Humedad;
		private MySQLPool mySQLPool;	
		private String nombre="";
		
		public void start(Promise<Void> future) {
			MySQLConnectOptions mySQLConnectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
					.setDatabase("particulas").setUser("root").setPassword("MEllamoeduar");
			PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
			mySQLPool = MySQLPool.pool(vertx, mySQLConnectOptions, poolOptions);
				
				

			
		
			TelegramOptions telegramOptioms= new TelegramOptions()
					.setBotName("ParticulasDADbot")
					.setBotToken("1175617937:AAHQiZfuCTV9Vx400OkMY1G5fPaxWr6sWTQ");
						
							
							
						
						bot= TelegramBot.create(vertx,telegramOptioms)
								.receiver(new LongPollingReceiver().onUpdate(handler->{
						if(handler.getMessage().getText().toLowerCase().contains("borrar tablas")) {
							WebClient client =WebClient.create(vertx);
							client.delete(8082,"localhost",
									"/api/restaurarValues")
							.send(ar->{
								if(ar.succeeded()) {
								
										
										 bot.sendMessage(new SendMessage()
												 .setText("Tablas borradas")
													.setChatId(handler.getMessage().getChatId()));
								}else {
									bot.sendMessage(new SendMessage()
											.setText("Algo no ha salido bien")
											.setChatId(handler.getMessage().getChatId()));
								}
							});;
						}else if(handler.getMessage().getText().toLowerCase().contains("hola")) {
							bot.sendMessage(new SendMessage()
									.setText("Hola "+ handler.getMessage().getFrom().getFirstName()+ " Puedo darte informacion sobre el tiempo y el numero de particulas que hay en el aire, primero dime "
											+ "que estacion quieres saber la informacion: Hospital o Casa")
									.setChatId(handler.getMessage().getChatId()));
						
								
							
//  GET_SENSOR
						}else if(handler.getMessage().getText().toLowerCase().contains("casa")) {

								WebClient client =WebClient.create(vertx);
								client.get(8082,"localhost",
										"/api/sensor/DHT11_values/1")
								.send(ar->{
									if(ar.succeeded()) {
									
											HttpResponse<Buffer> response= ar.result();
											System.out.println(response);
											JsonArray list= response.bodyAsJsonArray();
											JsonObject sensor=list.getJsonObject(list.size()-1);
											System.out.println(sensor);
											Temperatura= sensor.getFloat("temperatura");
											Humedad= sensor.getFloat("humedad");
											 bot.sendMessage(new SendMessage()
													 .setText("Sensor Casa:\n"
																+"Temperatura: "+Temperatura+"\n"
																+"Humedad: "+Humedad+"\n"
																)
														.setChatId(handler.getMessage().getChatId()));
									}else {
										bot.sendMessage(new SendMessage()
												.setText("Algo no ha salido bien, vuelva a escribir el actuador que quiere leer")
												.setChatId(handler.getMessage().getChatId()));
									}
								});;
								
								WebClient client2 =WebClient.create(vertx);
								client2.get(8082,"localhost",
										"/api/sensor/particulas_values/2")
								.send(ar->{
									if(ar.succeeded()) {
									
											HttpResponse<Buffer> response= ar.result();
											System.out.println(response);
											JsonArray list= response.bodyAsJsonArray();
											JsonObject sensor=list.getJsonObject(list.size()-1);
											Float Particulas_1= sensor.getFloat("particulas_1");
											Float Particulas_25= sensor.getFloat("particulas_25");
											Float Particulas_10= sensor.getFloat("particulas_10");
											 bot.sendMessage(new SendMessage()
													 .setText("Niveles de particulas\n\n"
															 +"Particulas 1mm: "+Particulas_1+"\n"
																+"Particulas 2,5mm: "+Particulas_25+"\n"
																+"Particulas 10mm: "+Particulas_10+"\n"
																)
														.setChatId(handler.getMessage().getChatId()));
									}else {
										bot.sendMessage(new SendMessage()
												.setText("Algo no ha salido bien, vuelva a escribir el actuador que quiere leer")
												.setChatId(handler.getMessage().getChatId()));
									}
								});;
//  GET_Actuador
						
								WebClient client3 =WebClient.create(vertx);
								client3.get(8082,"localhost",
										"/api/actuador/actuadorAlarmaValue/1")
								.send(ar->{
									if(ar.succeeded()) {
									
											HttpResponse<Buffer> response= ar.result();
											System.out.println(response);
											JsonArray list= response.bodyAsJsonArray();
											JsonObject actuador=list.getJsonObject(list.size()-1);
											Float value= actuador.getFloat("value");
											String mensaje="";
											if(value==1.0) {
												mensaje="Los niveles de particulas son mayores a a los recomendados"; 
											}
											if(value==2.0) {
												mensaje="Los niveles de particulas son muy perjudiciales"; 
											}
											if(value==0.0) {
												mensaje="Los niveles de particulas son buenos"; 
											}
											 bot.sendMessage(new SendMessage()
													 .setText(mensaje
																)
														.setChatId(handler.getMessage().getChatId()));
									}else {
										bot.sendMessage(new SendMessage()
												.setText("Algo no ha salido bien, vuelva a escribir el actuador que quiere leer")
												.setChatId(handler.getMessage().getChatId()));
									}
								});;
								WebClient client4 =WebClient.create(vertx);
								client4.get(80,"api.openweathermap.org",
										"/data/2.5/forecast?id=2519233&APPID=b4cbd393aaeabba55ca4b12efdabd913&units=metric")
								.send(ar->{
									if(ar.succeeded()) {
										
											HttpResponse<Buffer> response= ar.result();
											JsonObject object= response.bodyAsJsonObject();
											JsonArray list= object.getJsonArray("list");
											JsonObject lastWeather= list.getJsonObject(list.size()-1);
											String weather= lastWeather.getJsonArray("weather").getJsonObject(0).getString("description");
											Integer clouds= lastWeather.getJsonObject("clouds").getInteger("all");
											//Float description= lastWeather.getJsonObject("main").getJsonArray("weather").getFloat(2);
										bot.sendMessage(new SendMessage()
												.setText("El tiempo :\n"
														+"Temperatura: "+Temperatura+"\n"
														+"Humedad: "+Humedad+"\n"
														+"Nubes: "+clouds+" % \n"
														+"El dia sera: "+weather
														)
												.setChatId(handler.getMessage().getChatId()));
									}else {
										bot.sendMessage(new SendMessage()
												.setText("Algo no ha salido bien")
												.setChatId(handler.getMessage().getChatId()));
									}
								});;
						}else {
							bot.sendMessage(new SendMessage()
									.setText("No te entiendo, puedes volver a escribir")
									.setChatId(handler.getMessage().getChatId()));
						}
					}));
						
			bot.start();
		}
	
}
