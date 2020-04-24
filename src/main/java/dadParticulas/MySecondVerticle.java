package dadParticulas;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class MySecondVerticle extends AbstractVerticle{
	@Override
	public void start(Future<Void> starFuture) {
		vertx.eventBus().consumer("mensaje_p2p",mensaje ->{
			String stringMenssage= (String) mensaje.body();
			System.out.println("Mensaje recibido 2: " + stringMenssage);
			mensaje.reply("Si, llego el mensaje 2");
			
		});
		vertx.eventBus().consumer("mensaje_broadcast",mensaje ->{
			String stringMenssage= (String) mensaje.body();
			System.out.println("Mensaje broadcast 2: " + stringMenssage);
			
		});
	}
}
