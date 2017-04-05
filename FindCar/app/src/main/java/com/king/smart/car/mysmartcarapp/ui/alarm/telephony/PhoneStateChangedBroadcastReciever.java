/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.king.smart.car.mysmartcarapp.ui.alarm.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.king.smart.car.mysmartcarapp.ui.alarm.service.AlarmService;


public class PhoneStateChangedBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(getClass().getSimpleName(), "onReceive()");
		if (intent.getAction().equals("alarm.action")) {
			Intent serviceIntent = new Intent(context, AlarmService.class);
			serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(serviceIntent);
		}
	}

}
