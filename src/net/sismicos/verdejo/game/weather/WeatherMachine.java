package net.sismicos.verdejo.game.weather;

import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.logger.Logger;

public class WeatherMachine {
	// raining forecast for the next 10 minutes
	private boolean[] is_raining = new boolean[LENGTH_FORECAST];
	
	// length in minutes of the weather forecast
	private static final int LENGTH_FORECAST = 20;
	
	// constant increment/decrement in rain per second
	private static final float RAIN_INC = Game.RAIN_MAX/60f;
	
	// variability of rain in rain per second
	private static final float RAIN_VAR = RAIN_INC*1f;
	
	// wather machine internal time
	private long current_time = 0L;
	private static int prev_second = 0;
	
	public void init() {
		// always starts and without rain
		is_raining[0] = false;
		
		// the rest randomly changes, but the change is favored
		// at least 5 minutes or rain and dryness are required
		do {
			float prob = 0.5f;
			for(int i=1; i<LENGTH_FORECAST; ++i) {
				if (Math.random() < prob) {
					is_raining[i] = true;
					prob /= 2f;
				}
				else {
					is_raining[i] = false;
					prob *= 2f;
				}
			}
		} while(!checkForecast());
		
		// print the weather forecast in Debug
		String weather_forecast = new String("Weather forecast: ");
		for(int i=0; i<LENGTH_FORECAST; ++i) {
			if(is_raining[i]) {
				weather_forecast += "1 ";
			}
			else {
				weather_forecast += "0 ";
			}
		}
		Logger.printDebug(weather_forecast, 2);
	}
	
	public void update(int delta) {
		// update time
		current_time += (long) delta;
		current_time = current_time % (1000L*60L*(long)LENGTH_FORECAST);
		
		// get the current tendency
		int tendency = 0;
		int current_min = (int) Math.floor((double) current_time/1000f/60f);
		current_min %= LENGTH_FORECAST;
		int next_min = (current_min+1) % LENGTH_FORECAST;
		if(is_raining[current_min] != is_raining[next_min]) {
			if(is_raining[current_min]) {
				tendency = -1;
			}
			else {
				tendency = 1;
			}
		}
		
		// previous second
		int second = (int) Math.floor((float)current_time/1000f);
		if(second != prev_second) {
			prev_second = second;
			Logger.printDebug("Current time: " + current_time, 2);
			Logger.printDebug("Current rain rate: " + Game.getRainRate(), 2);
			Logger.printDebug("Current min: " + current_min + 
					" Next min: " + next_min, 2);
		}
		
		// apply the change
		float current_rain = Game.getRainRate();
		current_rain += (tendency*RAIN_INC + (Math.random() - 0.5)*RAIN_VAR)
				*delta/1000f;
		Game.setRainRate(current_rain);
	}
	
	/**
	 * Checks if the weather forecast is valid, i.e. has at least 5 minutes of
	 * sun and 5 minutes of rain.
	 * @return Whether the weather forecast is good or not.
	 */
	private boolean checkForecast() {
		int num_rain = 0;
		int num_dry = 0;
		for(int i=0; i<LENGTH_FORECAST; ++i) {
			if(is_raining[i]) {
				num_rain++;
			}
			else {
				num_dry++;
			}
		}
		
		if(num_dry >= 5 && num_rain >= 5) {
			return true;
		}
		else {
			return false;
		}
	}
}
