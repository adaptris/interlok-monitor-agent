package com.adaptris.monitor.agent.activity;

public abstract class EndpointActivity extends BaseFlowActivity {

  private static final long serialVersionUID = -6179345792118668407L;

  // TODO: Allow a destination per message id.
  private String destination;

  private String vendorImpClass;

  private WorkflowActivity parent;

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getVendorImpClass() {
    return vendorImpClass;
  }

  public void setVendorImpClass(String vendorImpClass) {
    this.vendorImpClass = vendorImpClass;
  }

  @Override
  public WorkflowActivity getParent() {
    return parent;
  }

  public void setParent(WorkflowActivity parent) {
    this.parent = parent;
  }

  public ChannelActivity getGrandParent() {
    return getParent() == null ? null : getParent().getParent();
  }

  public AdapterActivity getGreatGrandParent() {
    return getGrandParent() == null ? null : getGrandParent().getParent();
  }

}
