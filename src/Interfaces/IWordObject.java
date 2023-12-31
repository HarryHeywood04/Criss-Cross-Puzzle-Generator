package Interfaces;

import Enums.Direction;

public interface IWordObject {
    public int[] GetPos();
    public Direction GetRot();
    public String GetWord();
    public int GetLength();
}
