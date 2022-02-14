package org.cufir.plugin.mr.bean;

import lombok.Data;

/**
 * 传输对象
 * @author jin.c.li
 *
 */
@Data
public class TransferDataBean {
	
	private String id;
	private String childId;
	private String name;
	private String type;
	private String level;
	private String imgPath;
	private MrTreeItem treeListItem;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransferDataBean other = (TransferDataBean) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
