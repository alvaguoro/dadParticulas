package dadParticulas;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class WorkerVertice extends AbstractVerticle{
	@Override
	public void start(Future<Void> starFuture){
		
		
				try {
								
					System.out.println("Antes de dormir");
					Thread.sleep(10000);
					System.out.println("Despues de dormir");
					starFuture.complete();
				
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
					starFuture.fail(e);
				}
			
			
	}



}
