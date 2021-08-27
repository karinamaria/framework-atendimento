package br.ufrn.PDSgrupo5.framework.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Service;

@Service
public class DataHoraService {
	
	
	public Date converterParaDate(String data, String hora) throws ParseException {
		String stringData = data + " " + hora;
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date dataDate = formato.parse(stringData);
		
		return dataDate;
	}
	
	public String getDiaSemana(Date data) {
		GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(data);
    	int dia = gc.get(GregorianCalendar.DAY_OF_WEEK);

    	String diaSemana;
		switch(dia) {
			case 1:
				diaSemana = "Domingo";
				break;
			case 2:
				diaSemana = "Segunda-feira";
				break;
			case 3:
				diaSemana = "Terça-feira";
				break;
			case 4:
				diaSemana = "Quarta-feira";
				break;
			case 5:
				diaSemana = "Quinta-feira";
				break;
			case 6:
				diaSemana = "Sexta-feira";
				break;
			default: //case 7:
				diaSemana = "Sábado";
		}
		return diaSemana;
	}
}
