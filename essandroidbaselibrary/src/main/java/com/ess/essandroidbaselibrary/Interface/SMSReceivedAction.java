package com.ess.essandroidbaselibrary.Interface;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 1/1/2019.
 */

public interface SMSReceivedAction
{
    /**
     * Notify when SMS is received from any network to any Sim.
     * Need to attach this for "SMSReceiveHandler".
     *
     * @param sender ex:  +94712XXXXXX
     * @param messageBody as a String. combined message series is appended to same message.
     */
    public void onSMSReceived(String sender, String messageBody);
}
