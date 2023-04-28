package ma.enset.agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.util.Iterator;
import java.util.Random;

public class VendeurAgent extends GuiAgent {
    private VendeurGUI vendeurGUI;
    DFAgentDescription dfAgentDescription;

    @Override
    protected void setup() {
        vendeurGUI =(VendeurGUI)getArguments()[0];
        vendeurGUI.setVendeurAgent(this);
        ParallelBehaviour parallelBehaviour=new ParallelBehaviour();
        parallelBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                dfAgentDescription=new DFAgentDescription();
                try {
                    DFService.register(getAgent(),dfAgentDescription);
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });
        addBehaviour(parallelBehaviour);
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        ServiceDescription service=new ServiceDescription();
        service.setType(guiEvent.getParameter(1).toString());
        service.setName(guiEvent.getParameter(0).toString());
        dfAgentDescription.addServices(service);
        try {
            DFService.modify(this,dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

    }
}
