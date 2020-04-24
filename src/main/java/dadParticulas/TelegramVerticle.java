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

public class TelegramVerticle extends AbstractVerticle{
	 
	private TelegramBot bot;
	@Override
	public void start(Promise<Void> future) {
		TelegramOptions telegramOptioms= new TelegramOptions()
				.setBotName("ParticulasDADbot")
				.setBotToken("1175617937:AAHQiZfuCTV9Vx400OkMY1G5fPaxWr6sWTQ");
		bot= TelegramBot.create(vertx,telegramOptioms)
				.receiver(new LongPollingReceiver().onUpdate(handler->{
					if(handler.getMessage().getText().toLowerCase().contains("hola")) {
						bot.sendMessage(new SendMessage()
								.setText("Hola "+ handler.getMessage().getFrom().getFirstName()+ " ¿en que puedo ayudarte?")
								.setChatId(handler.getMessage().getChatId()));
					}else if(handler.getMessage().getText().toLowerCase().contains("tiempo")) {
						WebClient client =WebClient.create(vertx);
						client.get(80,"api.openweathermap.org",
								"/data/2.5/forecast?id=2519233&APPID=b4cbd393aaeabba55ca4b12efdabd913&units=metric")
						.send(ar->{
							if(ar.succeeded()) {
								
									HttpResponse<Buffer> response= ar.result();
									JsonObject object= response.bodyAsJsonObject();
									JsonArray list= object.getJsonArray("list");
									JsonObject lastWeather= list.getJsonObject(list.size()-1);
									Float temp= lastWeather.getJsonObject("main").getFloat("temp");
									Float humidity= lastWeather.getJsonObject("main").getFloat("humidity");
									String weather= lastWeather.getJsonArray("weather").getJsonObject(0).getString("description");
									Integer clouds= lastWeather.getJsonObject("clouds").getInteger("all");
									//Float description= lastWeather.getJsonObject("main").getJsonArray("weather").getFloat(2);
								bot.sendMessage(new SendMessage()
										.setText("El tiempo en Coria del Rio es:\n"
												+"Temperatura: "+temp+" grados\n"
												+"Humedad: "+humidity+" % \n"
												+"Nuves: "+clouds+" % \n"
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
	
