package dadParticulas;

import java.util.ArrayList;

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
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import tipos.Sensor;

public class TelegramBaseVerticle extends AbstractVerticle{
		 
		private TelegramBot bot;
		private MySQLPool mySQLPool;	
		@Override
		
		public void start(Promise<Void> future) {
			MySQLConnectOptions mySQLConnectOptions = new MySQLConnectOptions().setPort(3306).setHost("localhost")
					.setDatabase("particulas").setUser("root").setPassword("8419");
			PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
			mySQLPool = MySQLPool.pool(vertx, mySQLConnectOptions, poolOptions);
				
				


		
			TelegramOptions telegramOptioms= new TelegramOptions()
					.setBotName("ParticulasDADbot")
					.setBotToken("1175617937:AAHQiZfuCTV9Vx400OkMY1G5fPaxWr6sWTQ");
			bot= TelegramBot.create(vertx,telegramOptioms)
					.receiver(new LongPollingReceiver().onUpdate(handler->{
						
//	SALUDO
						if(handler.getMessage().getText().toLowerCase().contains("hola")) {
							bot.sendMessage(new SendMessage()
									.setText("Hola "+ handler.getMessage().getFrom().getFirstName()+ " ¿en que puedo ayudarte?")
									.setChatId(handler.getMessage().getChatId()));

//  GET_SENSOR
						}else if(handler.getMessage().getText().toLowerCase().contains("sensor")) {
							String num="";
							String numeros=handler.getMessage().getText().toString();
							String [] lista=numeros.split("");
							
							
							for(int i=0;i<lista.length;i++) {
								if(lista[i].contains("0")||lista[i].contains("1")||lista[i].contains("2")||lista[i].contains("3")||
										lista[i].contains("4")||lista[i].contains("5")||lista[i].contains("6")||lista[i].contains("7")||
										lista[i].contains("8")||lista[i].contains("9")) {
									num=num+lista[i];
								}
							}
								WebClient client =WebClient.create(vertx);
								client.get(8081,"localhost",
										"/api/sensor/"+num)
								.send(ar->{
									if(ar.succeeded()) {
									
											HttpResponse<Buffer> response= ar.result();
											System.out.println(response);
											JsonArray list= response.bodyAsJsonArray();
											JsonObject sensor=list.getJsonObject(list.size()-1);
											Integer id= sensor.getInteger("idSensor");
											 String tipo= sensor.getString("type");
											 String nombre= sensor.getString("name");
											 Integer idD= sensor.getInteger("idDevice");
											 String ubi= sensor.getString("ubicacion");
											 bot.sendMessage(new SendMessage()
													 .setText("Sensor:\n"
																+"id: "+id+"\n"
																+"tipo: "+tipo+"\n"
																+"nombre: "+nombre+"\n"
																+"idDevice: "+idD+"\n"
																+"ubicacion: "+ubi+"\n"
																)
														.setChatId(handler.getMessage().getChatId()));
									}else {
										bot.sendMessage(new SendMessage()
												.setText("Algo no ha salido bien, vuelva a escribir el actuador que quiere leer")
												.setChatId(handler.getMessage().getChatId()));
									}
								});;
//  GET_Actuador
						}else if(handler.getMessage().getText().toLowerCase().contains("actuador")) {
							String num="";
							String numeros=handler.getMessage().getText().toString();
							String [] lista=numeros.split("");
							
							
							for(int i=0;i<lista.length;i++) {
								if(lista[i].contains("0")||lista[i].contains("1")||lista[i].contains("2")||lista[i].contains("3")||
										lista[i].contains("4")||lista[i].contains("5")||lista[i].contains("6")||lista[i].contains("7")||
										lista[i].contains("8")||lista[i].contains("9")) {
									num=num+lista[i];
								}
							}
								WebClient client =WebClient.create(vertx);
								client.get(8081,"localhost",
										"/api/actuador/"+num)
								.send(ar->{
									if(ar.succeeded()) {
									
											HttpResponse<Buffer> response= ar.result();
											System.out.println(response);
											JsonArray list= response.bodyAsJsonArray();
											JsonObject sensor=list.getJsonObject(list.size()-1);
											Integer id= sensor.getInteger("idActuador");
											 String tipo= sensor.getString("type");
											 String nombre= sensor.getString("name");
											 Integer idD= sensor.getInteger("idDevice");
											 String ubi= sensor.getString("ubicacion");
											 bot.sendMessage(new SendMessage()
													 .setText("Actuador:\n"
																+"id: "+id+"\n"
																+"tipo: "+tipo+"\n"
																+"nombre: "+nombre+"\n"
																+"idDevice: "+idD+"\n"
																+"ubicacion: "+ubi+"\n"
																)
														.setChatId(handler.getMessage().getChatId()));
									}else {
										bot.sendMessage(new SendMessage()
												.setText("Algo no ha salido bien, vuelva a escribir el actuador que quiere leer")
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
