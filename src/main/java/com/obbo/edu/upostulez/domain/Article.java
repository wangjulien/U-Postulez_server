package com.obbo.edu.upostulez.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "article")
public class Article implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2994491835161913235L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	
	private String jianTiCi;
	private String fanTiCi;
	private String pinYin;
	
	@Column(columnDefinition = "TEXT")
	private String jianTiShiYi;
	private String liJu;
	private String yingYuDuiYingCiJu;
	

	public Article() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getJianTiCi() {
		return jianTiCi;
	}

	public void setJianTiCi(String jianTiCi) {
		this.jianTiCi = jianTiCi;
	}

	public String getFanTiCi() {
		return fanTiCi;
	}

	public void setFanTiCi(String fanTiCi) {
		this.fanTiCi = fanTiCi;
	}

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

	public String getJianTiShiYi() {
		return jianTiShiYi;
	}

	public void setJianTiShiYi(String jianTiShiYi) {
		this.jianTiShiYi = jianTiShiYi;
	}

	public String getLiJu() {
		return liJu;
	}

	public void setLiJu(String liJu) {
		this.liJu = liJu;
	}

	public String getYingYuDuiYingCiJu() {
		return yingYuDuiYingCiJu;
	}

	public void setYingYuDuiYingCiJu(String yingYuDuiYingCiJu) {
		this.yingYuDuiYingCiJu = yingYuDuiYingCiJu;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Article other = (Article) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Article [name=").append(name).append("]");
		return builder.toString();
	}
}
