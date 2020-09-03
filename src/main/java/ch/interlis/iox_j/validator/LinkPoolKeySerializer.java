package ch.interlis.iox_j.validator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ch.ehi.iox.objpool.impl.AbstractIomObjectSerializer;
import ch.ehi.iox.objpool.impl.Serializer;

public class LinkPoolKeySerializer extends AbstractIomObjectSerializer implements Serializer<LinkPoolKey> {

    @Override
    public byte[] getBytes(LinkPoolKey object) throws IOException {
        ByteArrayOutputStream  byteStream = new ByteArrayOutputStream();
        writeInt(byteStream,mapName2Idx(object.getClassName()));
        writeInt(byteStream,mapName2Idx(object.getRoleName()));
        writeString(byteStream,object.getOid());
        return byteStream.toByteArray();
    }

    @Override
    public LinkPoolKey getObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in=new ByteArrayInputStream(bytes);
        String className=mapIdx2Name(readInt(in));
        String roleName=mapIdx2Name(readInt(in));
        String oid=readString(in);
        return new LinkPoolKey(oid,className,roleName);
    }

}
