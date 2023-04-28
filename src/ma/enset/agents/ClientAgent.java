package ma.enset.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.util.*;

public class ClientAgent extends GuiAgent {
    private ClientGUI clientGUI;
    DFAgentDescription agentDescriptions[];
    @Override
    protected void setup() {
        clientGUI=(ClientGUI)getArguments()[0];
        clientGUI.setClientAgent(this);
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();

    }

    @Override
    protected void takeDown() {

    }

    @Override
    protected void beforeMove() {

    }

    @Override
    protected void afterMove() {

    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        DFAgentDescription dfAgentDescription=new DFAgentDescription();
        ServiceDescription service=new ServiceDescription();
        String type=guiEvent.getParameter(0).toString();
        service.setType(type);
        dfAgentDescription.addServices(service);
        try {
            agentDescriptions= DFService.search(this,dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        for (DFAgentDescription df:agentDescriptions) {
            Iterator<ServiceDescription> services=df.getAllServices();
            while (services.hasNext()){
                ServiceDescription description = services.next();
                String type1=description.getType();
                if (type1.equals(type)) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("Agent",df.getName().getLocalName());
                    item.put("Name", description.getName());
                    item.put("Type", type1);
                    clientGUI.setService(item);
                }
            }
        }
    }
}
