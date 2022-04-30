package br.com.goes.analyzer.converters;

public enum Columns {
	
	
	BANCO_ORIGEM,
	AGENCIA_ORIGEM,
	CONTA_ORIGEM,
	BANCO_DESTINO,
	AGENCIA_DESTINO,
	CONTA_DESTINO,
	VALOR_TRANSACAO,
	DATA_HORA_TRANSACAO;
	
	
	public static int getIndex(Columns c) throws Exception {
		Columns[] values = values();
		for (int i = 0 ; i < values.length ; i++)
			if (c.equals(values[i]))
				return i;
		throw new Exception("Not Found");
	}
}
