package com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web.interfaces_para_la_web;

import java.util.HashMap;
import java.util.List;

public interface OnTaskCompleted {

        void onTaskCompleted(List<List<HashMap<String, String>>> jsonObj);

}
