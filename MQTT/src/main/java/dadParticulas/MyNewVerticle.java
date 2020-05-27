package dadParticulas;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;

public class MyNewVerticle extends AbstractVerticle{
	@Override
	public void start(Future<Void> starFuture){
		
//		vertx.executeBlocking(future->{
//				try {
//								
//					System.out.println("Antes de dormir");
//					Thread.sleep(10000);
//					System.out.println("Despues de dormir");
//					future.complete("Ejecucion terminada");
//				
//				} catch (InterruptedException e) {
//					System.out.println(e.getMessage());
//					e.printStackTrace();
//					future.complete("Ejecucion terminada con excepcion");
//				}
//			}, res->{
//				System.out.println("El resultado ha sido: "+res.result().toString());
//			});
			DeploymentOptions deploymentOptions =new DeploymentOptions().setWorker(true);
			vertx.deployVerticle(WorkerVertice.class.getName(),deploymentOptions, res->{
				System.out.println(res.result().toString());
			});
	}

}
