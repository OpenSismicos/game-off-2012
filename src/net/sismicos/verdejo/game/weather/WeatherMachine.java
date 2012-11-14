package net.sismicos.verdejo.game.weather;

import net.sismicos.verdejo.game.Game;
import net.sismicos.verdejo.logger.Logger;

public class WeatherMachine {
	// raining forecast for the next 10 minutes
	private boolean[] is_raining = new boolean[LENGTH_FORECAST];
	
	// length in minutes of the weather forecast
	private static final int LENGTH_FORECAST = 20;
	
	// maximum value for rain
	private static final float RAIN_MAX = 250f;
	
	// constant increment/decrement in rain per second
	private static final float RAIN_INC = RAIN_MAX/60f;
	
	// variability of rain in rain per second
	private static final float RAIN_VAR = RAIN_INC*0.05f;
	
	// wather machine internal time
	private long time = 0L;
	
	public void init() {
		// always starts and without rain
		is_raining[0] = false;
		
		// the rest randomly changes
		for(int i=1; i<LENGTH_FORECAST; ++i) {
			is_raining[i] = (Math.random() >= 0.5);
		}
		
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
		time += delta;
		time = (time % 1000*60*LENGTH_FORECAST);
		
		// get the current tendency
		int tendency = 0;
		int current_min = (int) Math.floor((double) time/1000f/60f);
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
		
		// apply the change
		float current_rain = Game.getRainRate();
		current_rain += tendency*RAIN_INC + (Math.random() - 0.5)*RAIN_VAR;
		Game.setRainRate(current_rain);
	}
}
