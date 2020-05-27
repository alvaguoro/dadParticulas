package dadParticulas;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

public class MyFirstVerticle extends AbstractVerticle{
	 
	@SuppressWarnings("deprecation")
	@Override
	public void start(Future<Void> startFuture) {
		/*vertx.createHttpServer().requestHandler(
				request->{
					request.response().end(
						"<h1>Bienvenido</h1> ");
					
				}).listen(8081,result-> {
					if(result.succeeded()) {
						System.out.println("Todo correcto");
					}else {
						System.out.println("Algo ha fallado");
					}
				});*/
		//vertx.deployVerticle(MySecondVerticle.class.getName());
		//vertx.deployVerticle(MyThirdVerticle.class.getName());
		//vertx.deployVerticle(MyNewVerticle.class.getName());
		vertx.deployVerticle(DataBaseVerticle.class.getName());
		vertx.deployVerticle(TelegramVerticle.class.getName());
		/*EventBus eventBus= vertx.eventBus();
		vertx.setPeriodic(4000, action -> {
			eventBus.send("mensaje_p2p","Hola, esto es un mensaje¿te llega?",
			reply-> {
				if(reply.succeeded()) {
					String replyMenssage=
							(String) reply.result().body();
					System.out.println("Respuesta: "+ replyMenssage );
				}else {
					System.out.println("no ha habido respuesta");
				}
			});
		});
		vertx.setPeriodic(4000, action ->{
			eventBus.publish("mensaje_broadcast", "Esto es un mensaje de prueba");
		});*/
	}
	
}
