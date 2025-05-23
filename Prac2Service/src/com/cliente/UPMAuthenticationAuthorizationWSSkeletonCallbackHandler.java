/**
 * UPMAuthenticationAuthorizationWSSkeletonCallbackHandler.java
 *
 * <p>This file was auto-generated from WSDL by the Apache Axis2 version: 2.0.0 Built on : Mar 04,
 * 2025 (10:15:06 HST)
 */
package com.cliente;

/**
 * UPMAuthenticationAuthorizationWSSkeletonCallbackHandler Callback class, Users can extend this
 * class and implement their own receiveResult and receiveError methods.
 */
public abstract class UPMAuthenticationAuthorizationWSSkeletonCallbackHandler {

  protected Object clientData;

  /**
   * User can pass in any object that needs to be accessed once the NonBlocking Web service call is
   * finished and appropriate method of this CallBack is called.
   *
   * @param clientData Object mechanism by which the user can pass in user data that will be
   *     avilable at the time this callback is called.
   */
  public UPMAuthenticationAuthorizationWSSkeletonCallbackHandler(Object clientData) {
    this.clientData = clientData;
  }

  /** Please use this constructor if you don't want to set any clientData */
  public UPMAuthenticationAuthorizationWSSkeletonCallbackHandler() {
    this.clientData = null;
  }

  /** Get the client data */
  public Object getClientData() {
    return clientData;
  }

  /**
   * auto generated Axis2 call back method for removeUser method override this method for handling
   * normal response from removeUser operation
   */
  public void receiveResultremoveUser(
      com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserResponseE result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * removeUser operation
   */
  public void receiveErrorremoveUser(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for addUser method override this method for handling
   * normal response from addUser operation
   */
  public void receiveResultaddUser(
      com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.AddUserResponse result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * addUser operation
   */
  public void receiveErroraddUser(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for existUser method override this method for handling
   * normal response from existUser operation
   */
  public void receiveResultexistUser(
      com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.ExistUserResponseE result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * existUser operation
   */
  public void receiveErrorexistUser(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for login method override this method for handling normal
   * response from login operation
   */
  public void receiveResultlogin(
      com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.LoginResponse result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from login
   * operation
   */
  public void receiveErrorlogin(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for changePassword method override this method for
   * handling normal response from changePassword operation
   */
  public void receiveResultchangePassword(
      com.cliente.UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordResponseE result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * changePassword operation
   */
  public void receiveErrorchangePassword(java.lang.Exception e) {}
}
