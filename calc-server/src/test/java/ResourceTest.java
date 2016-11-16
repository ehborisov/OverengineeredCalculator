import com.example.calc.CalculatorResource;
import com.example.calc.beans.ExpressionData;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class ResourceTest extends JerseyTest {

    @Mock
    private CalculatorResource calcResource;

    @Override
    protected TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    @Override
    protected DeploymentContext configureDeployment() {
        MockitoAnnotations.initMocks(this);
        final AbstractBinder binder = new AbstractBinder() {

            @Override
            protected void configure() {
                bind(calcResource).to(CalculatorResource.class);
            }
        };

        final ResourceConfig config = new ResourceConfig()
                .register(binder)
                .register(CalculatorResource.class);
        return ServletDeploymentContext.forServlet(new ServletContainer(config)).build();
    }

    @Test
    public void resourceAccessTest() {
        final Entity<ExpressionData> entity = Entity.json(new ExpressionData().withExpression("2 + 2"));
        final Response response = target("/calculate").request().post(entity);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
}

