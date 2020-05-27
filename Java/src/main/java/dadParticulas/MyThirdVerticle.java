package dadParticulas;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class MyThirdVerticle extends AbstractVerticle{
	@Override
	public void start(Future<Void> starFuture) {
		vertx.eventBus().consumer("mensaje_p2p",mensaje ->{
			String stringMenssage= (String) mensaje.body();
			System.out.println("Mensaje recibido 3: " + stringMenssage);
			mensaje.reply("Si, llego el mensaje 3");
			
		});
		vertx.eventBus().consumer("mensaje_broadcast",mensaje ->{
			String stringMenssage= (String) mensaje.body();
			System.out.println("Mensaje broadcast 3: " + stringMenssage);
			
		});
	}
}
