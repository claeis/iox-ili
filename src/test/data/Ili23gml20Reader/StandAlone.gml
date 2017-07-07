<?xml version="1.0" encoding="UTF-8"?>
<ili:TRANSFER xmlns:ili="http://www.interlis.ch/ILIGML-2.0/INTERLIS" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.interlis.ch/ILIGML-2.0/Ili23" gml:id="iox1">
	<ili:baskets>
		<Schule gml:id="bidS">
			<member>
				<Kinder gml:id="oidK">
				</Kinder>
			</member>
			<member>
				<Lehrer gml:id="oidL">
				</Lehrer>
			</member>
			<member>
				<Beziehung gml:id="x1">
					<bezKinder xlink:href="#oidK"/>
					<bezLehrer xlink:href="#oidL"/>
				</Beziehung>
			</member>
		</Schule>
	</ili:baskets>
</ili:TRANSFER>