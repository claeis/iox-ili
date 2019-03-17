<?xml version="1.0" encoding="UTF-8"?>
<ili:TRANSFER xmlns:ili="http://www.interlis.ch/ILIGML-2.0/INTERLIS" xmlns:gml="http://www.opengis.net/gml/3.2" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.interlis.ch/ILIGML-2.0/Test1" gml:id="iox1">
	<ili:baskets>
		<TopicG gml:id="bid1">
			<member>
				<ClassG2 gml:id="x21">
					<attrRefG1 xlink:href="#x10"></attrRefG1>
				</ClassG2>
			</member>
			<member>
				<ClassG2 gml:id="x20">
					<attrRefG1 xlink:href="#x10"></attrRefG1>
				</ClassG2>
			</member>
			<member>
				<ClassG1 gml:id="x10">
					<ClassG2 xlink:href="#x20"></ClassG2>
					<ClassG2 xlink:href="#x20"></ClassG2>
					<ClassG3 xlink:href="#x30"></ClassG3>
				</ClassG1>
			</member>
			<member>
				<ClassG3 gml:id="x30">
					<attrRefG1 xlink:href="#x10"></attrRefG1>
				</ClassG3>
			</member>
			<member>
				<ClassG1 gml:id="x11"></ClassG1>
			</member>
		</TopicG>
	</ili:baskets>
</ili:TRANSFER>