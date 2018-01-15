package com.adaptris.monitor.agent.activity;

public class EndpointActivity extends BaseActivity {

  private static final long serialVersionUID = -6179345792118668407L;

  // TODO: Allow a destination per message id.
  private String destination;

  private String consumerClass;

  private String vendorImpClass;

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getConsumerClass() {
    return consumerClass;
  }

  public void setConsumerClass(String consumerClass) {
    this.consumerClass = consumerClass;
  }

  public String getVendorImpClass() {
    return vendorImpClass;
  }

  public void setVendorImpClass(String vendorImpClass) {
    this.vendorImpClass = vendorImpClass;
  }

}
