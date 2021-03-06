package grenc.masters.database.entities;

import grenc.masters.matchsticktask.type.MatchstickTaskStatus;

public class MatchstickTaskData
{
	private int id;
	private int taskSessionId;
	private int number;
	private int phaseNumber;
	
	private MatchstickTaskStatus status;
	
	private String originalEq;
	private String solvedEq;
	
	private Long time;
	private Long totalActivityTime;
	private int moves;
	private float transfer;
	
	
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getTaskSessionId()
	{
		return taskSessionId;
	}
	public void setTaskSessionId(int taskSessionId)
	{
		this.taskSessionId = taskSessionId;
	}
	
	public int getNumber()
	{
		return number;
	}
	public void setNumber(int number)
	{
		this.number = number;
	}
	
	public int getPhaseNumber()
	{
		return phaseNumber;
	}
	public void setPhaseNumber(int phaseNumber)
	{
		this.phaseNumber = phaseNumber;
	}
	
	public MatchstickTaskStatus getStatus()
	{
		return status;
	}
	public void setStatus(MatchstickTaskStatus status)
	{
		if (status == null)
			return;
		this.status = status;
	}
	public void setStatus(String status)
	{	
		if (status == null)
			return;
		this.status = MatchstickTaskStatus.valueOf(status);
	}

	public String getOriginalEq()
	{
		return originalEq;
	}
	public void setOriginalEq(String originalEq)
	{
		this.originalEq = originalEq;
	}
	public String getSolvedEq()
	{
		return solvedEq;
	}
	public void setSolvedEq(String solvedEq)
	{
		this.solvedEq = solvedEq;
	}
	public Long getTime()
	{
		return time;
	}
	public void setTime(Long time)
	{
		this.time = time;
	}
	public Long getTotalActivityTime()
	{
		return totalActivityTime;
	}
	public void setTotalActivityTime(Long time)
	{
		this.totalActivityTime = time;
	}
	public int getMoves()
	{
		return moves;
	}
	public void setMoves(int moves)
	{
		this.moves = moves;
	}
	public float getTransfer()
	{
		return transfer;
	}
	public void setTransfer(float transfer)
	{
		this.transfer = transfer;
	}
	
	@Override
	public String toString()
	{
		return "MatchstickTaskData [id=" + id + ", taskSessionId=" + taskSessionId + ", number=" + number
				+ ", phaseNumber=" + phaseNumber + ", status=" + status + ", originalEq=" + originalEq + ", solvedEq="
				+ solvedEq + ", time=" + time + ", totalActivityTime=" + totalActivityTime + ", moves=" + moves
				+ ", transfer=" + transfer + "]";
	}
	
}
